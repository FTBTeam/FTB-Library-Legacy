package com.feed_the_beast.ftblib.client;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.FTBLibCommon;
import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.commands.client.CmdFTBC;
import com.feed_the_beast.ftblib.events.client.RegisterGuiProvidersEvent;
import com.feed_the_beast.ftblib.events.player.IGuiProvider;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.client.ParticleColoredDust;
import com.feed_the_beast.ftblib.lib.cmd.CommandMirror;
import com.feed_the_beast.ftblib.lib.gui.misc.ChunkSelectorMap;
import com.feed_the_beast.ftblib.lib.icon.PlayerHeadIcon;
import com.feed_the_beast.ftblib.lib.io.DataReader;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.command.ICommand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class FTBLibClient extends FTBLibCommon implements IResourceManagerReloadListener
{
	public static final List<SidebarButtonGroup> SIDEBAR_BUTTON_GROUPS = new ArrayList<>();
	private static final Map<ResourceLocation, IGuiProvider> GUI_PROVIDERS = new HashMap<>();
	public static final Map<String, ClientConfig> CLIENT_CONFIG_MAP = new HashMap<>();
	public static final Collection<String> OPTIONAL_SERVER_MODS_CLIENT = new HashSet<>();
	public static UUID UNIVERSE_UUID = null;

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		FTBLibClientConfig.sync();
		new RegisterGuiProvidersEvent(GUI_PROVIDERS::put).post();
		ClientUtils.localPlayerHead = new PlayerHeadIcon(ClientUtils.MC.getSession().getProfile().getId());
		((IReloadableResourceManager) ClientUtils.MC.getResourceManager()).registerReloadListener(this);
		ChunkSelectorMap.setMap(new BuiltinChunkMap());
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
					for (JsonElement e : DataReader.get(resource).json().getAsJsonArray())
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

		JsonElement element = DataReader.get(new File(CommonUtils.folderLocal, "client/sidebar_buttons.json")).safeJson();
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
					JsonElement json = DataReader.get(resource).json();

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
		}

		for (String domain : manager.getResourceDomains())
		{
			try
			{
				for (IResource resource : manager.getAllResources(new ResourceLocation(domain, "sidebar_buttons.json")))
				{
					JsonElement json = DataReader.get(resource).json();

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

								if (!FTBLibConfig.debugging.dev_sidebar_buttons && buttonJson.has("dev_only") && buttonJson.get("dev_only").getAsBoolean())
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

								if (sidebarButtonConfig.has(button.id.getResourceDomain()))
								{
									JsonElement e = sidebarButtonConfig.get(button.id.getResourceDomain());

									if (e.isJsonObject() && e.getAsJsonObject().has(button.id.getResourcePath()))
									{
										button.setConfig(e.getAsJsonObject().get(button.id.getResourcePath()).getAsBoolean());
									}
								}
								else if (sidebarButtonConfig.has(button.id.toString()))
								{
									button.setConfig(sidebarButtonConfig.get(button.id.toString()).getAsBoolean());
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

		for (SidebarButtonGroup group : groupMap.values())
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
			for (ICommand command : cmd.getSubCommands())
			{
				ClientCommandHandler.instance.registerCommand(new CommandMirror(command));
			}
		}
	}

	@Override
	public void handleClientMessage(MessageToClient<?> message)
	{
		if (FTBLibConfig.debugging.log_network)
		{
			FTBLib.LOGGER.info("Net RX: " + message.getClass().getName());
		}

		message.onMessage();
	}

	@Override
	public void spawnDust(World world, double x, double y, double z, float r, float g, float b, float a)
	{
		ClientUtils.spawnParticle(new ParticleColoredDust(world, x, y, z, r, g, b, a));
	}

	@Override
	public long getWorldTime()
	{
		return ClientUtils.MC.world == null ? super.getWorldTime() : ClientUtils.MC.world.getTotalWorldTime();
	}

	@Nullable
	public static IGuiProvider getGui(ResourceLocation id)
	{
		return GUI_PROVIDERS.get(id);
	}

	public static void saveSidebarButtonConfig()
	{
		JsonObject o = new JsonObject();

		for (SidebarButtonGroup group : SIDEBAR_BUTTON_GROUPS)
		{
			for (SidebarButton button : group.getButtons())
			{
				JsonObject o1 = o.getAsJsonObject(button.id.getResourceDomain());

				if (o1 == null)
				{
					o1 = new JsonObject();
					o.add(button.id.getResourceDomain(), o1);
				}

				o1.addProperty(button.id.getResourcePath(), button.getConfig());
			}
		}

		JsonUtils.toJsonSafe(new File(CommonUtils.folderLocal, "client/sidebar_buttons.json"), o);
	}

	public static boolean isModLoadedOnServer(String modid)
	{
		return !modid.isEmpty() && OPTIONAL_SERVER_MODS_CLIENT.contains(modid);
	}

	public static boolean areAllModsLoadedOnServer(Collection<String> modids)
	{
		return modids.isEmpty() || OPTIONAL_SERVER_MODS_CLIENT.containsAll(modids);
	}
}