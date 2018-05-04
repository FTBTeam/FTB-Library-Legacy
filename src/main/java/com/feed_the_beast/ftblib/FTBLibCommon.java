package com.feed_the_beast.ftblib;

import com.feed_the_beast.ftblib.events.RegisterAdminPanelActionsEvent;
import com.feed_the_beast.ftblib.events.RegisterConfigValueProvidersEvent;
import com.feed_the_beast.ftblib.events.RegisterContainerProvidersEvent;
import com.feed_the_beast.ftblib.events.RegisterSyncDataEvent;
import com.feed_the_beast.ftblib.events.ServerReloadEvent;
import com.feed_the_beast.ftblib.events.player.IContainerProvider;
import com.feed_the_beast.ftblib.events.team.RegisterTeamGuiActionsEvent;
import com.feed_the_beast.ftblib.lib.EventHandler;
import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.config.ConfigValueProvider;
import com.feed_the_beast.ftblib.lib.config.IConfigCallback;
import com.feed_the_beast.ftblib.lib.config.RankConfigAPI;
import com.feed_the_beast.ftblib.lib.data.Action;
import com.feed_the_beast.ftblib.lib.data.ISyncData;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import com.feed_the_beast.ftblib.net.FTBLibNetHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.asm.ModAnnotation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class FTBLibCommon
{
	private static final EnumSet<Side> DEFAULT_SIDES = EnumSet.allOf(Side.class);
	public static final Map<String, ConfigValueProvider> CONFIG_VALUE_PROVIDERS = new HashMap<>();
	public static final Map<UUID, EditingConfig> TEMP_SERVER_CONFIG = new HashMap<>();
	public static final Map<ResourceLocation, IContainerProvider> GUI_CONTAINER_PROVIDERS = new HashMap<>();
	public static final Map<String, ISyncData> SYNCED_DATA = new HashMap<>();
	public static final HashSet<ResourceLocation> RELOAD_IDS = new HashSet<>();
	public static final Map<ResourceLocation, Action> TEAM_GUI_ACTIONS = new HashMap<>();
	public static final Map<ResourceLocation, Action> ADMIN_PANEL_ACTIONS = new HashMap<>();

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

	private void registerEventHandler(ASMDataTable.ASMData data, Side side) throws Exception
	{
		@SuppressWarnings("unchecked")
		List<ModAnnotation.EnumHolder> sidesEnum = (List<ModAnnotation.EnumHolder>) data.getAnnotationInfo().get("value");
		EnumSet<Side> sides = DEFAULT_SIDES;
		if (sidesEnum != null)
		{
			sides = EnumSet.noneOf(Side.class);

			for (ModAnnotation.EnumHolder h : sidesEnum)
			{
				sides.add(Side.valueOf(h.getValue()));
			}
		}

		if (sides != DEFAULT_SIDES && !sides.contains(side))
		{
			return;
		}

		@SuppressWarnings("unchecked")
		List<String> requiredMods = (List<String>) data.getAnnotationInfo().get("requiredMods");

		if (requiredMods != null && !requiredMods.isEmpty())
		{
			for (String s : requiredMods)
			{
				if (!isModLoaded(s.split(";")))
				{
					return;
				}
			}
		}

		Class<?> c = Class.forName(data.getObjectName());
		MinecraftForge.EVENT_BUS.register(c);
	}

	private static boolean isModLoaded(String[] mods)
	{
		for (String mod : mods)
		{
			if (mod.startsWith("!"))
			{
				if (Loader.isModLoaded(mod.substring(1)))
				{
					return false;
				}
			}
			else if (Loader.isModLoaded(mod))
			{
				return true;
			}
		}

		return false;
	}

	public void preInit(FMLPreInitializationEvent event)
	{
		FTBLib.LOGGER.info("Loading FTBLib, DevEnv:" + CommonUtils.DEV_ENV);
		CommonUtils.init(event.getModConfigurationDirectory());
		Side side = event.getSide();

		for (ASMDataTable.ASMData data : event.getAsmData().getAll(EventHandler.class.getName()))
		{
			try
			{
				registerEventHandler(data, side);
			}
			catch (Exception ex)
			{
			}
		}

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