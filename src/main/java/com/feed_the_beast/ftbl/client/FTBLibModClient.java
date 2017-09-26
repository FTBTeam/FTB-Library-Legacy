package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.ISidebarButton;
import com.feed_the_beast.ftbl.api.player.IGuiProvider;
import com.feed_the_beast.ftbl.api.player.RegisterGuiProvidersEvent;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.cmd.CmdFTBC;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.icon.PlayerHeadImage;
import com.feed_the_beast.ftbl.lib.net.MessageBase;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
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
import java.util.concurrent.Callable;

/**
 * @author LatvianModder
 */
public class FTBLibModClient extends FTBLibModCommon implements IResourceManagerReloadListener
{
	private static final Map<String, ISidebarButton> SIDEBAR_BUTTON_MAP = new HashMap<>();
	public static final List<ISidebarButton> SIDEBAR_BUTTONS = new ArrayList<>();
	private static final ISidebarButton DBUTTON_BEFORE_ALL = new SidebarButton(new ResourceLocation("before_all"));
	private static final ISidebarButton DBUTTON_AFTER_ALL = new SidebarButton(new ResourceLocation("after_all"));
	private static final ISidebarButton DBUTTON_BEFORE = new SidebarButton(new ResourceLocation("before"));
	private static final ISidebarButton DBUTTON_AFTER = new SidebarButton(new ResourceLocation("after"));
	private static final Map<ResourceLocation, IGuiProvider> GUI_PROVIDERS = new HashMap<>();
	public static final Map<String, ClientConfig> CLIENT_CONFIG_MAP = new HashMap<>();

	private static class MessageTask implements Callable<Object>
	{
		private final MessageBase<?> message;

		private MessageTask(MessageBase<?> m)
		{
			message = m;
		}

		@Override
		@Nullable
		public Object call() throws Exception
		{
			message.onMessage(CommonUtils.cast(message), ClientUtils.MC.player);

			if (FTBLibAPI_Impl.LOG_NET)
			{
				CommonUtils.DEV_LOGGER.info("RX MessageBase: " + message.getClass().getName());
			}

			return null;
		}
	}

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		FTBLibClientConfig.sync();
		new RegisterGuiProvidersEvent(GUI_PROVIDERS::put).post();
		ClientUtils.localPlayerHead = new PlayerHeadImage(ClientUtils.MC.getSession().getProfile().getName());
		((IReloadableResourceManager) ClientUtils.MC.getResourceManager()).registerReloadListener(this);
	}

	@Override
	public void onResourceManagerReload(IResourceManager manager)
	{
		SIDEBAR_BUTTON_MAP.clear();
		CLIENT_CONFIG_MAP.clear();

		for (String domain : manager.getResourceDomains())
		{
			try
			{
				for (IResource resource : manager.getAllResources(new ResourceLocation(domain, "client_config.json")))
				{
					for (JsonElement e : JsonUtils.fromJson(resource).getAsJsonArray())
					{
						ClientConfig c = new ClientConfig(e.getAsJsonObject());
						CLIENT_CONFIG_MAP.put(c.id, c);
					}
				}
			}
			catch (Exception ex)
			{
				if (!(ex instanceof FileNotFoundException))
				{
					ex.printStackTrace();
				}
			}
		}

		JsonElement element = JsonUtils.fromJson(new File(CommonUtils.folderLocal, "client/sidebar_buttons.json"));
		JsonObject sidebarButtonConfig;

		if (element.isJsonObject())
		{
			sidebarButtonConfig = element.getAsJsonObject();
		}
		else
		{
			sidebarButtonConfig = new JsonObject();
		}

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

								if (button.getDefaultConfig() != null)
								{
									if (sidebarButtonConfig.has(button.getName()))
									{
										button.setConfig(sidebarButtonConfig.get(button.getName()).getAsBoolean());
									}
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

		TopologicalSort.DirectedGraph<ISidebarButton> sidebarButtonGraph = new TopologicalSort.DirectedGraph<>();
		sidebarButtonGraph.addNode(DBUTTON_BEFORE_ALL);
		sidebarButtonGraph.addNode(DBUTTON_BEFORE);
		sidebarButtonGraph.addNode(DBUTTON_AFTER_ALL);
		sidebarButtonGraph.addNode(DBUTTON_AFTER);
		sidebarButtonGraph.addEdge(DBUTTON_BEFORE, DBUTTON_AFTER);
		sidebarButtonGraph.addEdge(DBUTTON_BEFORE_ALL, DBUTTON_BEFORE);
		sidebarButtonGraph.addEdge(DBUTTON_AFTER, DBUTTON_AFTER_ALL);

		SIDEBAR_BUTTON_MAP.values().forEach(sidebarButtonGraph::addNode);

		for (ISidebarButton button : SIDEBAR_BUTTON_MAP.values())
		{
			boolean preDepAdded = false;
			boolean postDepAdded = false;

			for (Map.Entry<String, Boolean> entry : button.getDependencies().entrySet())
			{
				if (entry.getValue())
				{
					for (String id : button.getDependencies().keySet())
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

		List<ISidebarButton> sortedSidebarButtonList = TopologicalSort.topologicalSort(sidebarButtonGraph);
		sortedSidebarButtonList.removeAll(Arrays.asList(DBUTTON_BEFORE_ALL, DBUTTON_BEFORE, DBUTTON_AFTER, DBUTTON_AFTER_ALL));
		SIDEBAR_BUTTONS.clear();
		SIDEBAR_BUTTONS.addAll(sortedSidebarButtonList);
		saveSidebarButtonConfig();
	}

	@Override
	public void postInit(LoaderState.ModState state)
	{
		super.postInit(state);

		CmdFTBC cmd = new CmdFTBC();
		ClientCommandHandler.instance.registerCommand(cmd);

		if (FTBLibClientConfig.general.mirror_commands)
		{
			cmd.getSubCommands().forEach(ClientCommandHandler.instance::registerCommand);
		}
	}

	@Override
	public void handleClientMessage(MessageBase<?> message)
	{
		ClientUtils.MC.addScheduledTask(new MessageTask(message));
	}

	@Nullable
	public static IGuiProvider getGui(ResourceLocation id)
	{
		return GUI_PROVIDERS.get(id);
	}

	public static void saveSidebarButtonConfig()
	{
		JsonObject o = new JsonObject();

		for (ISidebarButton button : SIDEBAR_BUTTONS)
		{
			if (button.getDefaultConfig() != null)
			{
				o.addProperty(button.getName(), button.getDefaultConfig());
			}
		}

		JsonUtils.toJson(new File(CommonUtils.folderLocal, "client/sidebar_buttons.json"), o);
	}
}