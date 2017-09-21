package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.EventHandler;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.IContainerProvider;
import com.feed_the_beast.ftbl.api.IDataProvider;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.ISyncData;
import com.feed_the_beast.ftbl.api.events.ReloadEvent;
import com.feed_the_beast.ftbl.api.events.registry.RegisterConfigValueProvidersEvent;
import com.feed_the_beast.ftbl.api.events.registry.RegisterContainerProvidersEvent;
import com.feed_the_beast.ftbl.api.events.registry.RegisterDataProvidersEvent;
import com.feed_the_beast.ftbl.api.events.registry.RegisterGuideLineProvidersEvent;
import com.feed_the_beast.ftbl.api.events.registry.RegisterOptionalServerModsEvent;
import com.feed_the_beast.ftbl.api.events.registry.RegisterRankConfigEvent;
import com.feed_the_beast.ftbl.api.events.registry.RegisterSyncDataEvent;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.api_impl.SharedServerData;
import com.feed_the_beast.ftbl.lib.AsmHelper;
import com.feed_the_beast.ftbl.lib.NBTDataStorage;
import com.feed_the_beast.ftbl.lib.config.ConfigBlockState;
import com.feed_the_beast.ftbl.lib.config.ConfigBoolean;
import com.feed_the_beast.ftbl.lib.config.ConfigColor;
import com.feed_the_beast.ftbl.lib.config.ConfigDouble;
import com.feed_the_beast.ftbl.lib.config.ConfigEnum;
import com.feed_the_beast.ftbl.lib.config.ConfigGroup;
import com.feed_the_beast.ftbl.lib.config.ConfigInt;
import com.feed_the_beast.ftbl.lib.config.ConfigItemStack;
import com.feed_the_beast.ftbl.lib.config.ConfigList;
import com.feed_the_beast.ftbl.lib.config.ConfigNull;
import com.feed_the_beast.ftbl.lib.config.ConfigString;
import com.feed_the_beast.ftbl.lib.config.ConfigStringEnum;
import com.feed_the_beast.ftbl.lib.config.ConfigTextComponent;
import com.feed_the_beast.ftbl.lib.config.ConfigTristate;
import com.feed_the_beast.ftbl.lib.config.ConfigValueProvider;
import com.feed_the_beast.ftbl.lib.config.IConfigCallback;
import com.feed_the_beast.ftbl.lib.config.RankConfigValueInfo;
import com.feed_the_beast.ftbl.lib.guide.GuideContentsLine;
import com.feed_the_beast.ftbl.lib.guide.GuideExtendedTextLine;
import com.feed_the_beast.ftbl.lib.guide.GuideHrLine;
import com.feed_the_beast.ftbl.lib.guide.GuideImageLine;
import com.feed_the_beast.ftbl.lib.guide.GuideListLine;
import com.feed_the_beast.ftbl.lib.guide.GuidePage;
import com.feed_the_beast.ftbl.lib.guide.GuideSwitchLine;
import com.feed_the_beast.ftbl.lib.guide.GuideTextLineString;
import com.feed_the_beast.ftbl.lib.guide.IconAnimationLine;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.net.MessageBase;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.feed_the_beast.ftbl.net.FTBLibNetHandler;
import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.asm.ModAnnotation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class FTBLibModCommon
{
	private static final EnumSet<Side> DEFAULT_SIDES = EnumSet.allOf(Side.class);
	public static final Map<String, ConfigValueProvider> CONFIG_VALUE_PROVIDERS = new HashMap<>();
	public static final Map<UUID, EditingConfig> TEMP_SERVER_CONFIG = new HashMap<>();
	public static final Map<ResourceLocation, IContainerProvider> GUI_CONTAINER_PROVIDERS = new HashMap<>();
	public static final Map<String, ISyncData> SYNCED_DATA = new HashMap<>();
	public static final Map<ResourceLocation, IDataProvider<IForgePlayer>> DATA_PROVIDER_PLAYER = new HashMap<>();
	public static final Map<ResourceLocation, IDataProvider<IForgeTeam>> DATA_PROVIDER_TEAM = new HashMap<>();
	private static final Map<String, RankConfigValueInfo> RANK_CONFIGS = new HashMap<>();
	public static final Map<String, RankConfigValueInfo> RANK_CONFIGS_MIRROR = Collections.unmodifiableMap(RANK_CONFIGS);
	public static final HashSet<ResourceLocation> RELOAD_IDS = new HashSet<>();

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
		FTBLibFinals.LOGGER.info("Loading FTBLib, DevEnv:" + CommonUtils.DEV_ENV);
		FTBLibAPI.API = new FTBLibAPI_Impl();
		CommonUtils.init(event.getModConfigurationDirectory());
		Side side = event.getSide();

		for (ASMDataTable.ASMData data : AsmHelper.getASMData(event.getAsmData(), EventHandler.class))
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

		RegisterOptionalServerModsEvent optionalServerMods = new RegisterOptionalServerModsEvent(SharedServerData.INSTANCE.optionalServerMods::add);
		optionalServerMods.register(FTBLibFinals.MOD_ID);
		optionalServerMods.post();

		RegisterConfigValueProvidersEvent configValues = new RegisterConfigValueProvidersEvent(CONFIG_VALUE_PROVIDERS::put);
		configValues.register(ConfigNull.ID, () -> ConfigNull.INSTANCE);
		configValues.register(ConfigList.ID, () -> new ConfigList(ConfigNull.ID));
		configValues.register(ConfigBoolean.ID, ConfigBoolean::new);
		configValues.register(ConfigTristate.ID, ConfigTristate::new);
		configValues.register(ConfigInt.ID, ConfigInt::new);
		configValues.register(ConfigDouble.ID, ConfigDouble::new);
		configValues.register(ConfigString.ID, ConfigString::new);
		configValues.register(ConfigColor.ID, ConfigColor::new);
		configValues.register(ConfigEnum.ID, ConfigStringEnum::new);
		configValues.register(ConfigBlockState.ID, ConfigBlockState::new);
		configValues.register(ConfigItemStack.ID, ConfigItemStack::new);
		configValues.register(ConfigTextComponent.ID, ConfigTextComponent::new);
		configValues.post();

		RegisterGuideLineProvidersEvent guideLines = new RegisterGuideLineProvidersEvent((id, provider) -> GuidePage.LINE_PROVIDERS.put(id.toLowerCase(), provider));
		guideLines.register("img", (page, json) -> new GuideImageLine(json));
		guideLines.register("image", (page, json) -> new GuideImageLine(json));
		guideLines.register("text_component", (page, json) -> new GuideExtendedTextLine(json));
		guideLines.register("text", (page, json) -> new GuideTextLineString(json));
		guideLines.register("list", GuideListLine::new);
		guideLines.register("hr", (page, json) -> new GuideHrLine(json));
		guideLines.register("animation", (page, json) -> new IconAnimationLine(json));
		guideLines.register("switch", GuideSwitchLine::new);
		guideLines.register("contents", (page, json) -> new GuideContentsLine(page));
		guideLines.post();

		new RegisterDataProvidersEvent.Player(DATA_PROVIDER_PLAYER::put).post();
		new RegisterDataProvidersEvent.Team(DATA_PROVIDER_TEAM::put).post();
		new RegisterContainerProvidersEvent(GUI_CONTAINER_PROVIDERS::put).post();
		new RegisterSyncDataEvent(SYNCED_DATA::put).post();
		new RegisterRankConfigEvent((id, defPlayer, defOP) ->
		{
			Preconditions.checkArgument(!RANK_CONFIGS.containsKey(id), "Duplicate RankConfigKey ID found: " + id);
			RankConfigValueInfo c = new RankConfigValueInfo(id, defPlayer, defOP);
			RANK_CONFIGS.put(c.id, c);
			return c;
		}).post();
		new ReloadEvent.RegisterIds(RELOAD_IDS::add).post();
	}

	public void postInit(LoaderState.ModState state)
	{
		reloadConfig(state);
	}

	public void reloadConfig(LoaderState.ModState state)
	{
		JsonElement overridesE = JsonUtils.fromJson(new File(CommonUtils.folderConfig, "config_overrides.json"));

		if (overridesE.isJsonObject())
		{
		}
	}

	public <T> NBTDataStorage createDataStorage(T owner, Map<ResourceLocation, IDataProvider<T>> map)
	{
		NBTDataStorage storage = new NBTDataStorage();

		for (Map.Entry<ResourceLocation, IDataProvider<T>> entry : map.entrySet())
		{
			try
			{
				storage.add(entry.getKey(), entry.getValue().getData(owner));
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		return storage.isEmpty() ? NBTDataStorage.EMPTY : storage;
	}

	public void handleClientMessage(MessageBase<?> message)
	{
	}
}