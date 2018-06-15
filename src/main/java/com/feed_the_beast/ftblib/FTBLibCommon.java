package com.feed_the_beast.ftblib;

import com.feed_the_beast.ftblib.events.FTBLibPreInitRegistryEvent;
import com.feed_the_beast.ftblib.events.IReloadHandler;
import com.feed_the_beast.ftblib.events.ServerReloadEvent;
import com.feed_the_beast.ftblib.lib.EnumReloadType;
import com.feed_the_beast.ftblib.lib.config.ConfigBlockState;
import com.feed_the_beast.ftblib.lib.config.ConfigBoolean;
import com.feed_the_beast.ftblib.lib.config.ConfigColor;
import com.feed_the_beast.ftblib.lib.config.ConfigDouble;
import com.feed_the_beast.ftblib.lib.config.ConfigEnum;
import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.config.ConfigInt;
import com.feed_the_beast.ftblib.lib.config.ConfigItemStack;
import com.feed_the_beast.ftblib.lib.config.ConfigList;
import com.feed_the_beast.ftblib.lib.config.ConfigNull;
import com.feed_the_beast.ftblib.lib.config.ConfigString;
import com.feed_the_beast.ftblib.lib.config.ConfigStringEnum;
import com.feed_the_beast.ftblib.lib.config.ConfigTextComponent;
import com.feed_the_beast.ftblib.lib.config.ConfigTimer;
import com.feed_the_beast.ftblib.lib.config.ConfigTristate;
import com.feed_the_beast.ftblib.lib.config.ConfigValueProvider;
import com.feed_the_beast.ftblib.lib.config.IConfigCallback;
import com.feed_the_beast.ftblib.lib.config.RankConfigAPI;
import com.feed_the_beast.ftblib.lib.data.AdminPanelAction;
import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.feed_the_beast.ftblib.lib.data.FTBLibTeamGuiActions;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ISyncData;
import com.feed_the_beast.ftblib.lib.data.TeamAction;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import com.feed_the_beast.ftblib.net.FTBLibNetHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * @author LatvianModder
 */
public class FTBLibCommon
{
	public static final Map<String, ConfigValueProvider> CONFIG_VALUE_PROVIDERS = new HashMap<>();
	public static final Map<UUID, EditingConfig> TEMP_SERVER_CONFIG = new HashMap<>();
	public static final Map<String, ISyncData> SYNCED_DATA = new HashMap<>();
	public static final HashMap<ResourceLocation, IReloadHandler> RELOAD_IDS = new HashMap<>();
	public static final Map<ResourceLocation, TeamAction> TEAM_GUI_ACTIONS = new HashMap<>();
	public static final Map<ResourceLocation, AdminPanelAction> ADMIN_PANEL_ACTIONS = new HashMap<>();
	private static final Map<String, Function<ForgePlayer, ITextComponent>> CHAT_FORMATTING_SUBSTITUTES = new HashMap<>();

	public static Function<String, ITextComponent> chatFormattingSubstituteFunction(ForgePlayer player)
	{
		return s -> {
			Function<ForgePlayer, ITextComponent> sub = CHAT_FORMATTING_SUBSTITUTES.get(s);
			return sub == null ? null : sub.apply(player);
		};
	}

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

		FTBLibPreInitRegistryEvent.Registry registry = new FTBLibPreInitRegistryEvent.Registry()
		{
			@Override
			public void registerConfigValueProvider(String id, ConfigValueProvider provider)
			{
				CONFIG_VALUE_PROVIDERS.put(id, provider);
			}

			@Override
			public void registerSyncData(String mod, ISyncData data)
			{
				SYNCED_DATA.put(mod, data);
			}

			@Override
			public void registerServerReloadHandler(ResourceLocation id, IReloadHandler handler)
			{
				RELOAD_IDS.put(id, handler);
			}

			@Override
			public void registerAdminPanelAction(AdminPanelAction action)
			{
				ADMIN_PANEL_ACTIONS.put(action.getId(), action);
			}

			@Override
			public void registerTeamAction(TeamAction action)
			{
				TEAM_GUI_ACTIONS.put(action.getId(), action);
			}
		};

		registry.registerConfigValueProvider(ConfigNull.ID, () -> ConfigNull.INSTANCE);
		registry.registerConfigValueProvider(ConfigList.ID, () -> new ConfigList(ConfigNull.ID));
		registry.registerConfigValueProvider(ConfigBoolean.ID, ConfigBoolean::new);
		registry.registerConfigValueProvider(ConfigTristate.ID, ConfigTristate::new);
		registry.registerConfigValueProvider(ConfigInt.ID, ConfigInt::new);
		registry.registerConfigValueProvider(ConfigDouble.ID, ConfigDouble::new);
		registry.registerConfigValueProvider(ConfigString.ID, ConfigString::new);
		registry.registerConfigValueProvider(ConfigColor.ID, ConfigColor::new);
		registry.registerConfigValueProvider(ConfigEnum.ID, ConfigStringEnum::new);
		registry.registerConfigValueProvider(ConfigBlockState.ID, ConfigBlockState::new);
		registry.registerConfigValueProvider(ConfigItemStack.ID, ConfigItemStack::new);
		registry.registerConfigValueProvider(ConfigTextComponent.ID, ConfigTextComponent::new);
		registry.registerConfigValueProvider(ConfigTimer.ID, ConfigTimer::new);

		registry.registerServerReloadHandler(new ResourceLocation(FTBLib.MOD_ID, "config"), reloadEvent -> FTBLibConfig.sync());

		registry.registerAdminPanelAction(new AdminPanelAction(FTBLib.MOD_ID, "reload", GuiIcons.REFRESH, -1000)
		{
			@Override
			public Type getType(ForgePlayer player, NBTTagCompound data)
			{
				return Type.fromBoolean(player.isOP());
			}

			@Override
			public void onAction(ForgePlayer player, NBTTagCompound data)
			{
				FTBLibAPI.reloadServer(player.team.universe, player.getPlayer(), EnumReloadType.RELOAD_COMMAND, ServerReloadEvent.ALL);
			}
		}.setTitle(new TextComponentTranslation("ftblib.lang.reload_server_button")));

		registry.registerTeamAction(FTBLibTeamGuiActions.CONFIG);
		registry.registerTeamAction(FTBLibTeamGuiActions.INFO);
		registry.registerTeamAction(FTBLibTeamGuiActions.MEMBERS);
		registry.registerTeamAction(FTBLibTeamGuiActions.ALLIES);
		registry.registerTeamAction(FTBLibTeamGuiActions.MODERATORS);
		registry.registerTeamAction(FTBLibTeamGuiActions.ENEMIES);
		registry.registerTeamAction(FTBLibTeamGuiActions.LEAVE);
		registry.registerTeamAction(FTBLibTeamGuiActions.TRANSFER_OWNERSHIP);

		new FTBLibPreInitRegistryEvent(registry).post();

		RankConfigAPI.getHandler();

		CHAT_FORMATTING_SUBSTITUTES.put("name", ForgePlayer::getDisplayName);
		CHAT_FORMATTING_SUBSTITUTES.put("team", player -> player.team.getTitle());
		//CHAT_FORMATTING_SUBSTITUTES.put("tag", player -> new TextComponentString(player.getTag()));
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