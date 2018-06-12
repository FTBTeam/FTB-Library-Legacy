package com.feed_the_beast.ftblib;

import com.feed_the_beast.ftblib.events.RegisterAdminPanelActionsEvent;
import com.feed_the_beast.ftblib.events.RegisterConfigValueProvidersEvent;
import com.feed_the_beast.ftblib.events.RegisterContainerProvidersEvent;
import com.feed_the_beast.ftblib.events.RegisterSyncDataEvent;
import com.feed_the_beast.ftblib.events.ServerReloadEvent;
import com.feed_the_beast.ftblib.events.player.IContainerProvider;
import com.feed_the_beast.ftblib.events.team.RegisterTeamGuiActionsEvent;
import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.config.ConfigValueProvider;
import com.feed_the_beast.ftblib.lib.config.IConfigCallback;
import com.feed_the_beast.ftblib.lib.config.RankConfigAPI;
import com.feed_the_beast.ftblib.lib.data.AdminPanelAction;
import com.feed_the_beast.ftblib.lib.data.ISyncData;
import com.feed_the_beast.ftblib.lib.data.TeamAction;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import com.feed_the_beast.ftblib.net.FTBLibNetHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class FTBLibCommon
{
	public static final Map<String, ConfigValueProvider> CONFIG_VALUE_PROVIDERS = new HashMap<>();
	public static final Map<UUID, EditingConfig> TEMP_SERVER_CONFIG = new HashMap<>();
	public static final Map<ResourceLocation, IContainerProvider> GUI_CONTAINER_PROVIDERS = new HashMap<>();
	public static final Map<String, ISyncData> SYNCED_DATA = new HashMap<>();
	public static final HashSet<ResourceLocation> RELOAD_IDS = new HashSet<>();
	public static final Map<ResourceLocation, TeamAction> TEAM_GUI_ACTIONS = new HashMap<>();
	public static final Map<ResourceLocation, AdminPanelAction> ADMIN_PANEL_ACTIONS = new HashMap<>();

	public static class EditingConfig
	{
		public final ConfigGroup group;
		public final IConfigCallback callback;

		public EditingConfig(ConfigGroup g, IConfigCallback c)
		{
			group = g;
			callback = c;
		}
	}

	public void preInit(FMLPreInitializationEvent event)
	{
		FTBLib.LOGGER.info("Loading FTBLib, DevEnv:" + CommonUtils.DEV_ENV);
		CommonUtils.init(event.getModConfigurationDirectory());
		FTBLibNetHandler.init();

		new RegisterConfigValueProvidersEvent(CONFIG_VALUE_PROVIDERS::put).post();
		new RegisterContainerProvidersEvent(GUI_CONTAINER_PROVIDERS::put).post();
		new RegisterSyncDataEvent(SYNCED_DATA::put).post();
		RankConfigAPI.getHandler();
		new ServerReloadEvent.RegisterIds(RELOAD_IDS::add).post();
		new RegisterTeamGuiActionsEvent(action -> TEAM_GUI_ACTIONS.put(action.getId(), action)).post();
		new RegisterAdminPanelActionsEvent(action -> ADMIN_PANEL_ACTIONS.put(action.getId(), action)).post();
	}

	public void postInit()
	{
	}

	/*
	public void reloadConfig(LoaderState.ModState state)
	{
		JsonElement overridesE = JsonUtils.fromJson(new File(CommonUtils.folderConfig, "config_overrides.json"));

		if (overridesE.isJsonObject())
		{
		}
	}*/

	public void handleClientMessage(MessageToClient message)
	{
	}

	public void spawnDust(World world, double x, double y, double z, float r, float g, float b, float a)
	{
	}

	public void spawnDust(World world, double x, double y, double z, Color4I col)
	{
		spawnDust(world, x, y, z, col.redf(), col.greenf(), col.bluef(), col.alphaf());
	}

	public long getWorldTime()
	{
		return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0).getTotalWorldTime();
	}
}