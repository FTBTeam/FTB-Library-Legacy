package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.IFTBLibClientRegistry;
import com.feed_the_beast.ftbl.api.config.IConfigFile;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.events.FTBLibClientRegistryEvent;
import com.feed_the_beast.ftbl.api.gui.IGuiProvider;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.cmd.CmdFTBC;
import com.feed_the_beast.ftbl.lib.SidebarButton;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.client.PlayerHeadImage;
import com.feed_the_beast.ftbl.lib.config.ConfigFile;
import com.feed_the_beast.ftbl.lib.config.ConfigKey;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiConfigs;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiGuide;
import com.feed_the_beast.ftbl.lib.guide.GuidePage;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.net.MessageBase;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.toposort.TopologicalSort;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class FTBLibModClient extends FTBLibModCommon implements IFTBLibClientRegistry, IResourceManagerReloadListener
{
	private IConfigFile clientConfig;
	private static final Map<String, SidebarButton> SIDEBAR_BUTTON_MAP = new HashMap<>();
	private static final List<SidebarButton> SIDEBAR_BUTTONS = new ArrayList<>();
	private static final SidebarButton DBUTTON_BEFORE_ALL = new SidebarButton(new ResourceLocation("before_all"));
	private static final SidebarButton DBUTTON_AFTER_ALL = new SidebarButton(new ResourceLocation("after_all"));
	private static final SidebarButton DBUTTON_BEFORE = new SidebarButton(new ResourceLocation("before"));
	private static final SidebarButton DBUTTON_AFTER = new SidebarButton(new ResourceLocation("after"));
	private static final Map<ResourceLocation, IGuiProvider> GUI_PROVIDERS = new HashMap<>();

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);

		clientConfig = new ConfigFile(new TextComponentTranslation("sidebar_button.ftbl.settings"), () -> new File(CommonUtils.folderLocal, "client_config.json"));

		String group = FTBLibFinals.MOD_ID;
		addClientConfig(group, "item_ore_names", FTBLibClientConfig.ITEM_ORE_NAMES);
		addClientConfig(group, "action_buttons_on_top", FTBLibClientConfig.ACTION_BUTTONS_ON_TOP);
		addClientConfig(group, "ignore_nei", FTBLibClientConfig.IGNORE_NEI);
		addClientConfig(group, "notifications", FTBLibClientConfig.NOTIFICATIONS);
		addClientConfig(group, "replace_status_message_with_notification", FTBLibClientConfig.REPLACE_STATUS_MESSAGE_WITH_NOTIFICATION);
		group = FTBLibFinals.MOD_ID + ".gui";
		addClientConfig(group, "enable_chunk_selector_depth", GuiConfigs.ENABLE_CHUNK_SELECTOR_DEPTH);
		group = FTBLibFinals.MOD_ID + ".gui.info";
		addClientConfig(group, "border_width", GuiConfigs.INFO_BORDER_WIDTH).addFlags(IConfigKey.USE_SCROLL_BAR);
		addClientConfig(group, "border_height", GuiConfigs.INFO_BORDER_HEIGHT).addFlags(IConfigKey.USE_SCROLL_BAR);
		addClientConfig(group, "color_background", GuiConfigs.INFO_BACKGROUND);
		addClientConfig(group, "color_text", GuiConfigs.INFO_TEXT);

		new FTBLibClientRegistryEvent(this).post();

		//For Dev reasons
		GameProfile profile = ClientUtils.MC.getSession().getProfile();
		if (profile.getId().equals(StringUtils.fromString("5afb9a5b207d480e887967bc848f9a8f")))
		{
			CommonUtils.userIsLatvianModder = true;
		}

		ClientUtils.localPlayerHead = new PlayerHeadImage(profile.getName());
		((IReloadableResourceManager) ClientUtils.MC.getResourceManager()).registerReloadListener(this);
	}

	@Override
	public void onResourceManagerReload(IResourceManager manager)
	{
		SIDEBAR_BUTTON_MAP.clear();
		clientConfig.getTree().entrySet().removeIf(entry -> entry.getKey().getGroup().equals("sidebar_button"));

		for (String domain : manager.getResourceDomains())
		{
			try
			{
				for (IResource resource : manager.getAllResources(new ResourceLocation(domain, "sidebar_buttons.json")))
				{
					JsonElement json = JsonUtils.fromJson(new InputStreamReader(resource.getInputStream()));

					if (json.isJsonObject())
					{
						for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet())
						{
							if (entry.getValue().isJsonObject())
							{
								SidebarButton button = new SidebarButton(new ResourceLocation(domain, entry.getKey()), entry.getValue().getAsJsonObject());

								if (button.devOnly && !CommonUtils.DEV_ENV)
								{
									continue;
								}

								SIDEBAR_BUTTON_MAP.put(button.getName(), button);

								if (button.config != null)
								{
									ConfigKey key = new ConfigKey(button.getName(), button.config.copy());
									key.setGroup("sidebar_button");
									key.setNameLangKey("sidebar_button." + key.getName());
									key.setInfoLangKey("sidebar_button." + key.getName() + ".info");
									clientConfig.add(key, button.config);
								}
							}
						}
					}
				}
			}
			catch (Exception ex)
			{
				//CommonUtils.DEV_LOGGER.info("Error while loading guide from domain '" + domain + "'");

				if (!(ex instanceof FileNotFoundException))
				{
					ex.printStackTrace();
				}
			}
		}

		TopologicalSort.DirectedGraph<SidebarButton> sidebarButtonGraph = new TopologicalSort.DirectedGraph<>();
		sidebarButtonGraph.addNode(DBUTTON_BEFORE_ALL);
		sidebarButtonGraph.addNode(DBUTTON_BEFORE);
		sidebarButtonGraph.addNode(DBUTTON_AFTER_ALL);
		sidebarButtonGraph.addNode(DBUTTON_AFTER);
		sidebarButtonGraph.addEdge(DBUTTON_BEFORE, DBUTTON_AFTER);
		sidebarButtonGraph.addEdge(DBUTTON_BEFORE_ALL, DBUTTON_BEFORE);
		sidebarButtonGraph.addEdge(DBUTTON_AFTER, DBUTTON_AFTER_ALL);

		SIDEBAR_BUTTON_MAP.values().forEach(sidebarButtonGraph::addNode);

		for (SidebarButton button : SIDEBAR_BUTTON_MAP.values())
		{
			boolean preDepAdded = false;
			boolean postDepAdded = false;

			for (Map.Entry<String, Boolean> entry : button.dependencies.entrySet())
			{
				if (entry.getValue())
				{
					for (String id : button.dependencies.keySet())
					{
						postDepAdded = true;

						if (id.equals("*"))
						{
							sidebarButtonGraph.addEdge(DBUTTON_BEFORE_ALL, button);
							sidebarButtonGraph.addEdge(button, DBUTTON_BEFORE);
							preDepAdded = true;
						}
						else
						{
							sidebarButtonGraph.addEdge(button, DBUTTON_AFTER);
							if (SIDEBAR_BUTTON_MAP.containsKey(id))
							{
								sidebarButtonGraph.addEdge(button, SIDEBAR_BUTTON_MAP.get(id));
							}
						}
					}
				}
				else
				{
					preDepAdded = true;
					String id = entry.getKey();

					if (id.equals("*"))
					{
						sidebarButtonGraph.addEdge(button, DBUTTON_AFTER_ALL);
						sidebarButtonGraph.addEdge(DBUTTON_AFTER, button);
						postDepAdded = true;
					}
					else
					{
						sidebarButtonGraph.addEdge(DBUTTON_BEFORE, button);
						if (SIDEBAR_BUTTON_MAP.containsKey(id))
						{
							sidebarButtonGraph.addEdge(SIDEBAR_BUTTON_MAP.get(id), button);
						}
					}
				}
			}

			if (!preDepAdded)
			{
				sidebarButtonGraph.addEdge(DBUTTON_BEFORE, button);
			}

			if (!postDepAdded)
			{
				sidebarButtonGraph.addEdge(button, DBUTTON_AFTER);
			}
		}

		List<SidebarButton> sortedSidebarButtonList = TopologicalSort.topologicalSort(sidebarButtonGraph);
		sortedSidebarButtonList.removeAll(Arrays.asList(DBUTTON_BEFORE_ALL, DBUTTON_BEFORE, DBUTTON_AFTER, DBUTTON_AFTER_ALL));
		SIDEBAR_BUTTONS.clear();
		SIDEBAR_BUTTONS.addAll(sortedSidebarButtonList);
		clientConfig.load();
		clientConfig.save();
	}

	@Override
	public void postInit(LoaderState.ModState state)
	{
		super.postInit(state);
		ClientCommandHandler.instance.registerCommand(new CmdFTBC());
	}

	@Override
	public void loadAllFiles()
	{
		super.loadAllFiles();
		clientConfig.load();
	}

	@Override
	public void saveAllFiles()
	{
		super.saveAllFiles();
		clientConfig.save();
	}

	@Override
	public IConfigKey addClientConfig(String group, String id, IConfigValue value)
	{
		ConfigKey key = new ConfigKey(id, value.copy(), group, "client_config");
		clientConfig.add(key, value);
		return key;
	}

	@Override
	public void addGui(ResourceLocation id, IGuiProvider provider)
	{
		GUI_PROVIDERS.put(id, provider);
	}

	@Override
	@Nullable
	public IConfigFile getClientConfig()
	{
		return clientConfig;
	}

	@Override
	public void handleClientMessage(MessageBase<?> message)
	{
		ClientUtils.MC.addScheduledTask(() ->
		{
			message.onMessage(CommonUtils.cast(message), ClientUtils.MC.player);

			if (FTBLibAPI_Impl.LOG_NET)
			{
				CommonUtils.DEV_LOGGER.info("RX MessageBase: " + message.getClass().getName());
			}
		});
	}

	@Override
	public void displayGuide(GuidePage page)
	{
		new GuiGuide(page).openGui();
	}

	public static List<SidebarButton> getSidebarButtons(boolean ignoreConfig)
	{
		if (ignoreConfig)
		{
			return SIDEBAR_BUTTONS;
		}

		List<SidebarButton> list = new ArrayList<>();

		for (SidebarButton button : SIDEBAR_BUTTONS)
		{
			if (button.isVisible() && (button.config == null || button.config.getBoolean()))
			{
				list.add(button);
			}
		}

		return list;
	}

	@Nullable
	public static IGuiProvider getGui(ResourceLocation id)
	{
		return GUI_PROVIDERS.get(id);
	}
}