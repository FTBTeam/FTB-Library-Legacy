package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.FTBLibConfig;
import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.ISidebarButton;
import com.feed_the_beast.ftbl.api.ISidebarButtonGroup;
import com.feed_the_beast.ftbl.api.player.IGuiProvider;
import com.feed_the_beast.ftbl.api.player.RegisterGuiProvidersEvent;
import com.feed_the_beast.ftbl.cmd.CmdFTBC;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.icon.PlayerHeadIcon;
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
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author LatvianModder
 */
public class FTBLibModClient extends FTBLibModCommon implements IResourceManagerReloadListener
{
	public static final List<ISidebarButtonGroup> SIDEBAR_BUTTON_GROUPS = new ArrayList<>();
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

			if (FTBLibConfig.general.log_net)
			{
				CommonUtils.DEV_LOGGER.info("Net RX: " + message.getClass().getName());
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
		ClientUtils.localPlayerHead = new PlayerHeadIcon(ClientUtils.MC.getSession().getProfile().getName());
		((IReloadableResourceManager) ClientUtils.MC.getResourceManager()).registerReloadListener(this);
	}

	@Override
	public void onResourceManagerReload(IResourceManager manager)
	{
		SIDEBAR_BUTTON_GROUPS.clear();
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

		Map<ResourceLocation, SidebarButtonGroup> groupMap = new HashMap<>();

		for (String domain : manager.getResourceDomains())
		{
			try
			{
				for (IResource resource : manager.getAllResources(new ResourceLocation(domain, "sidebar_button_groups.json")))
				{
					JsonElement json = JsonUtils.fromJson(resource);

					for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet())
					{
						if (entry.getValue().isJsonObject())
						{
							JsonObject groupJson = entry.getValue().getAsJsonObject();
							int y = 0;

							if (groupJson.has("y"))
							{
								y = groupJson.get("y").getAsInt();
							}

							SidebarButtonGroup group = new SidebarButtonGroup(new ResourceLocation(domain, entry.getKey()), y);
							groupMap.put(group.getId(), group);
						}
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

			try
			{
				for (IResource resource : manager.getAllResources(new ResourceLocation(domain, "sidebar_buttons.json")))
				{
					JsonElement json = JsonUtils.fromJson(resource);

					if (json.isJsonObject())
					{
						for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet())
						{
							if (entry.getValue().isJsonObject())
							{
								JsonObject buttonJson = entry.getValue().getAsJsonObject();

								if (!buttonJson.has("group"))
								{
									continue;
								}

								if (!CommonUtils.DEV_ENV && buttonJson.has("dev_only") && buttonJson.get("dev_only").getAsBoolean())
								{
									continue;
								}

								SidebarButtonGroup group = groupMap.get(new ResourceLocation(buttonJson.get("group").getAsString()));

								if (group == null)
								{
									continue;
								}

								SidebarButton button = new SidebarButton(new ResourceLocation(domain, entry.getKey()), group, buttonJson);

								group.getButtons().add(button);

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
				if (!(ex instanceof FileNotFoundException))
				{
					ex.printStackTrace();
				}
			}
		}

		for (ISidebarButtonGroup group : groupMap.values())
		{
			if (!group.getButtons().isEmpty())
			{
				group.getButtons().sort(null);
				SIDEBAR_BUTTON_GROUPS.add(group);
			}
		}

		SIDEBAR_BUTTON_GROUPS.sort(null);
		saveSidebarButtonConfig();
	}

	@Override
	public void postInit()
	{
		super.postInit();

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

		for (ISidebarButtonGroup group : FTBLibAPI.API.getSidebarButtonGroups())
		{
			for (ISidebarButton button : group.getButtons())
			{
				if (button.getDefaultConfig() != null)
				{
					o.addProperty(button.getName(), button.getDefaultConfig());
				}
			}
		}

		JsonUtils.toJson(new File(CommonUtils.folderLocal, "client/sidebar_buttons.json"), o);
	}
}