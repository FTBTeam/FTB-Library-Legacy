package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.EventHandler;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.IDataProvider;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.IRankConfig;
import com.feed_the_beast.ftbl.api.ISyncData;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigFile;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.feed_the_beast.ftbl.api.events.ConfigLoadedEvent;
import com.feed_the_beast.ftbl.api.events.ReloadEvent;
import com.feed_the_beast.ftbl.api.events.registry.RegisterConfigEvent;
import com.feed_the_beast.ftbl.api.events.registry.RegisterContainerProvidersEvent;
import com.feed_the_beast.ftbl.api.events.registry.RegisterDataProvidersEvent;
import com.feed_the_beast.ftbl.api.events.registry.RegisterGuideLineProvidersEvent;
import com.feed_the_beast.ftbl.api.events.registry.RegisterOptionalServerModsEvent;
import com.feed_the_beast.ftbl.api.events.registry.RegisterRankConfigEvent;
import com.feed_the_beast.ftbl.api.events.registry.RegisterSyncDataEvent;
import com.feed_the_beast.ftbl.api.gui.IContainerProvider;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.api_impl.SharedServerData;
import com.feed_the_beast.ftbl.lib.AsmHelper;
import com.feed_the_beast.ftbl.lib.LangKey;
import com.feed_the_beast.ftbl.lib.NBTDataStorage;
import com.feed_the_beast.ftbl.lib.config.ConfigFile;
import com.feed_the_beast.ftbl.lib.config.ConfigKey;
import com.feed_the_beast.ftbl.lib.config.PropertyBlockState;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.config.PropertyByte;
import com.feed_the_beast.ftbl.lib.config.PropertyColor;
import com.feed_the_beast.ftbl.lib.config.PropertyDouble;
import com.feed_the_beast.ftbl.lib.config.PropertyEnumAbstract;
import com.feed_the_beast.ftbl.lib.config.PropertyInt;
import com.feed_the_beast.ftbl.lib.config.PropertyItemStack;
import com.feed_the_beast.ftbl.lib.config.PropertyJson;
import com.feed_the_beast.ftbl.lib.config.PropertyList;
import com.feed_the_beast.ftbl.lib.config.PropertyNull;
import com.feed_the_beast.ftbl.lib.config.PropertyShort;
import com.feed_the_beast.ftbl.lib.config.PropertyString;
import com.feed_the_beast.ftbl.lib.config.PropertyStringEnum;
import com.feed_the_beast.ftbl.lib.config.PropertyTextComponent;
import com.feed_the_beast.ftbl.lib.config.PropertyTristate;
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
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.asm.ModAnnotation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
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
	public static final Map<String, IConfigValueProvider> CONFIG_VALUE_PROVIDERS = new HashMap<>();
	public static final Map<String, IConfigFile> CONFIG_FILES = new HashMap<>();
	public static final Map<UUID, IConfigContainer> TEMP_SERVER_CONFIG = new HashMap<>();
	public static final Map<ResourceLocation, IContainerProvider> GUI_CONTAINER_PROVIDERS = new HashMap<>();
	public static final Map<String, ISyncData> SYNCED_DATA = new HashMap<>();
	public static final Map<ResourceLocation, IDataProvider<IUniverse>> DATA_PROVIDER_UNIVERSE = new HashMap<>();
	public static final Map<ResourceLocation, IDataProvider<IForgePlayer>> DATA_PROVIDER_PLAYER = new HashMap<>();
	public static final Map<ResourceLocation, IDataProvider<IForgeTeam>> DATA_PROVIDER_TEAM = new HashMap<>();
	private static final Map<String, IRankConfig> RANK_CONFIGS = new HashMap<>();
	public static final Map<String, IRankConfig> RANK_CONFIGS_MIRROR = Collections.unmodifiableMap(RANK_CONFIGS);
	public static final HashSet<ResourceLocation> RELOAD_IDS = new HashSet<>();

	private static class RankConfig extends ConfigKey implements IRankConfig
	{
		private final IConfigValue defaultOPValue;

		private RankConfig(String s, IConfigValue def, IConfigValue defOP)
		{
			super(s, def);
			defaultOPValue = def.copy();
			defaultOPValue.fromJson(defOP.getSerializableElement());
		}

		@Override
		public IConfigValue getDefOPValue()
		{
			return defaultOPValue;
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

		RegisterConfigEvent config = new RegisterConfigEvent((group0, id, value) ->
		{
			int i = group0.indexOf('.');

			String file;
			String group;

			if (i >= 0)
			{
				file = group0.substring(0, i);
				group = group0.substring(i + 1);
			}
			else
			{
				file = group0;
				group = "";
			}

			ConfigKey key = new ConfigKey(id, value.copy(), group, "config");
			key.setGroup(group0);
			key.setNameLangKey("config." + group0 + "." + id + ".name");
			key.setInfoLangKey("config." + group0 + "." + id + ".info");
			IConfigFile configFile = CONFIG_FILES.computeIfAbsent(file, f -> new ConfigFile(new TextComponentString(f), ConfigFile.NULL_FILE_PROVIDER));
			configFile.add(key, value);
			return key;
		}, (id, provider) ->
		{
			if (id.charAt(0) != '-')
			{
				id = id.toLowerCase();
				ConfigFile configFile = new ConfigFile(LangKey.of("config_group." + id + ".name").textComponent(), provider);
				CONFIG_FILES.put(id, configFile);
			}
		}, (id, provider) -> CONFIG_VALUE_PROVIDERS.put(id.toLowerCase(), provider));

		config.registerFile(FTBLibFinals.MOD_ID, () -> new File(CommonUtils.folderLocal, "ftbl.json"));
		config.registerValue(PropertyNull.ID, () -> PropertyNull.INSTANCE);
		config.registerValue(PropertyList.ID, () -> new PropertyList(PropertyNull.ID));
		config.registerValue(PropertyBool.ID, PropertyBool::new);
		config.registerValue(PropertyTristate.ID, PropertyTristate::new);
		config.registerValue(PropertyByte.ID, PropertyByte::new);
		config.registerValue(PropertyShort.ID, PropertyShort::new);
		config.registerValue(PropertyInt.ID, PropertyInt::new);
		config.registerValue(PropertyDouble.ID, PropertyDouble::new);
		config.registerValue(PropertyString.ID, PropertyString::new);
		config.registerValue(PropertyColor.ID, PropertyColor::new);
		config.registerValue(PropertyEnumAbstract.ID, PropertyStringEnum::new);
		config.registerValue(PropertyJson.ID, PropertyJson::new);
		config.registerValue(PropertyBlockState.ID, PropertyBlockState::new);
		config.registerValue(PropertyItemStack.ID, PropertyItemStack::new);
		config.registerValue(PropertyTextComponent.ID, PropertyTextComponent::new);

		String group = FTBLibFinals.MOD_ID;
		config.register(group, "clientless_mode", FTBLibConfig.CLIENTLESS_MODE);
		config.register(group, "mirror_ftb_commands", FTBLibConfig.MIRROR_FTB_COMMANDS);
		config.register(group, "merge_offline_mode_players", FTBLibConfig.MERGE_OFFLINE_MODE_PLAYERS);
		group = FTBLibFinals.MOD_ID + ".teams";
		config.register(group, "autocreate_on_login", FTBLibConfig.AUTOCREATE_TEAMS);
		config.register(group, "max_team_chat_history", FTBLibConfig.MAX_TEAM_CHAT_HISTORY);
		config.post();

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

		new RegisterDataProvidersEvent.Universe(DATA_PROVIDER_UNIVERSE::put).post();
		new RegisterDataProvidersEvent.Player(DATA_PROVIDER_PLAYER::put).post();
		new RegisterDataProvidersEvent.Team(DATA_PROVIDER_TEAM::put).post();
		new RegisterContainerProvidersEvent(GUI_CONTAINER_PROVIDERS::put).post();
		new RegisterSyncDataEvent(SYNCED_DATA::put).post();
		new RegisterRankConfigEvent((id, defPlayer, defOP) ->
		{
			Preconditions.checkArgument(!RANK_CONFIGS.containsKey(id), "Duplicate RankConfig ID found: " + id);
			RankConfig c = new RankConfig(id, defPlayer, defOP);
			c.setNameLangKey("rank_config." + id + ".name");
			c.setInfoLangKey("rank_config." + id + ".info");
			RANK_CONFIGS.put(c.getName(), c);
			return c;
		}).post();
		new ReloadEvent.RegisterIds(RELOAD_IDS::add).post();
	}

	public void postInit(LoaderState.ModState state)
	{
		reloadConfig(state);

		for (IConfigFile file : CONFIG_FILES.values())
		{
			file.save();
		}
	}

	public void loadAllFiles()
	{
		CONFIG_FILES.values().forEach(IConfigFile::load);
	}

	public void saveAllFiles()
	{
		CONFIG_FILES.values().forEach(IConfigFile::save);
	}

	public void reloadConfig(LoaderState.ModState state)
	{
		loadAllFiles();

		JsonElement overridesE = JsonUtils.fromJson(new File(CommonUtils.folderConfig, "config_overrides.json"));

		if (overridesE.isJsonObject())
		{
			overridesE.getAsJsonObject().entrySet().forEach(entry ->
			{
				if (entry.getValue().isJsonObject())
				{
					IConfigFile file = CONFIG_FILES.get(entry.getKey());

					if (file != null)
					{
						file.fromJson(entry.getValue());
					}
				}
			});

			if (state.ordinal() >= LoaderState.ModState.POSTINITIALIZED.ordinal())
			{
				saveAllFiles();
			}
		}

		new ConfigLoadedEvent(state).post();
	}

	public void worldLoaded()
	{
		//Cache data here if any
	}

	@Nullable
	public <T> NBTDataStorage createDataStorage(T owner, Map<ResourceLocation, IDataProvider<T>> map)
	{
		NBTDataStorage storage = new NBTDataStorage();

		map.forEach((key, value) ->
		{
			try
			{
				storage.add(key, value.getData(owner));
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		});

		return storage.isEmpty() ? null : storage;
	}

	@Nullable
	public IConfigFile getClientConfig()
	{
		return null;
	}

	public void handleClientMessage(MessageBase<?> message)
	{
	}

	public void displayGuide(GuidePage page)
	{
	}
}