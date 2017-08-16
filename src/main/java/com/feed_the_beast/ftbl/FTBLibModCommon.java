package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.EventHandler;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.IDataProvider;
import com.feed_the_beast.ftbl.api.IFTBLibRegistry;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.IRankConfig;
import com.feed_the_beast.ftbl.api.ISyncData;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigFile;
import com.feed_the_beast.ftbl.api.config.IConfigFileProvider;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.feed_the_beast.ftbl.api.events.ConfigLoadedEvent;
import com.feed_the_beast.ftbl.api.events.FTBLibRegistryEvent;
import com.feed_the_beast.ftbl.api.gui.IContainerProvider;
import com.feed_the_beast.ftbl.api.guide.IGuideTextLineProvider;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.api_impl.SharedServerData;
import com.feed_the_beast.ftbl.lib.AsmHelper;
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
import com.feed_the_beast.ftbl.lib.guide.DrawableObjectListLine;
import com.feed_the_beast.ftbl.lib.guide.GuideContentsLine;
import com.feed_the_beast.ftbl.lib.guide.GuideExtendedTextLine;
import com.feed_the_beast.ftbl.lib.guide.GuideHrLine;
import com.feed_the_beast.ftbl.lib.guide.GuideImageLine;
import com.feed_the_beast.ftbl.lib.guide.GuideListLine;
import com.feed_the_beast.ftbl.lib.guide.GuidePage;
import com.feed_the_beast.ftbl.lib.guide.GuideSwitchLine;
import com.feed_the_beast.ftbl.lib.guide.GuideTextLineString;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.net.MessageBase;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.feed_the_beast.ftbl.net.FTBLibNetHandler;
import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class FTBLibModCommon implements IFTBLibRegistry
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

		addOptionalServerMod(FTBLibFinals.MOD_ID);
		addConfigFileProvider(FTBLibFinals.MOD_ID, () -> new File(CommonUtils.folderLocal, "ftbl.json"));

		String group = FTBLibFinals.MOD_ID;
		addConfig(group, "mirror_ftb_commands", FTBLibConfig.MIRROR_FTB_COMMANDS);
		addConfig(group, "merge_offline_mode_players", FTBLibConfig.MERGE_OFFLINE_MODE_PLAYERS);
		group = FTBLibFinals.MOD_ID + ".teams";
		addConfig(group, "autocreate_on_login", FTBLibConfig.AUTOCREATE_TEAMS);
		addConfig(group, "max_team_chat_history", FTBLibConfig.MAX_TEAM_CHAT_HISTORY);

		addConfigValueProvider(PropertyNull.ID, () -> PropertyNull.INSTANCE);
		addConfigValueProvider(PropertyList.ID, () -> new PropertyList(PropertyNull.ID));
		addConfigValueProvider(PropertyBool.ID, PropertyBool::new);
		addConfigValueProvider(PropertyTristate.ID, PropertyTristate::new);
		addConfigValueProvider(PropertyByte.ID, PropertyByte::new);
		addConfigValueProvider(PropertyShort.ID, PropertyShort::new);
		addConfigValueProvider(PropertyInt.ID, PropertyInt::new);
		addConfigValueProvider(PropertyDouble.ID, PropertyDouble::new);
		addConfigValueProvider(PropertyString.ID, PropertyString::new);
		addConfigValueProvider(PropertyColor.ID, PropertyColor::new);
		addConfigValueProvider(PropertyEnumAbstract.ID, PropertyStringEnum::new);
		addConfigValueProvider(PropertyJson.ID, PropertyJson::new);
		addConfigValueProvider(PropertyBlockState.ID, PropertyBlockState::new);
		addConfigValueProvider(PropertyItemStack.ID, PropertyItemStack::new);
		addConfigValueProvider(PropertyTextComponent.ID, PropertyTextComponent::new);

		addInfoTextLine("img", (page, json) -> new GuideImageLine(json));
		addInfoTextLine("image", (page, json) -> new GuideImageLine(json));
		addInfoTextLine("text_component", (page, json) -> new GuideExtendedTextLine(json));
		addInfoTextLine("text", (page, json) -> new GuideTextLineString(json));
		addInfoTextLine("list", GuideListLine::new);
		addInfoTextLine("hr", (page, json) -> new GuideHrLine(json));
		addInfoTextLine("item_list", (page, json) -> new DrawableObjectListLine(json));
		addInfoTextLine("switch", GuideSwitchLine::new);
		addInfoTextLine("contents", (page, json) -> new GuideContentsLine(page));

		new FTBLibRegistryEvent(this).post();
	}

	public void postInit(LoaderState.ModState state)
	{
		reloadConfig(state);

		for (IConfigFile file : CONFIG_FILES.values())
		{
			file.save();
		}
	}

	public void loadRegistries()
	{
	}

	@Override
	public void addConfigFileProvider(String id, IConfigFileProvider provider)
	{
		if (id.charAt(0) != '-')
		{
			id = id.toLowerCase();
			ConfigFile configFile = new ConfigFile(new TextComponentTranslation("config_group." + id + ".name"), provider);
			CONFIG_FILES.put(id, configFile);
		}
	}

	@Override
	public void addConfigValueProvider(String id, IConfigValueProvider provider)
	{
		CONFIG_VALUE_PROVIDERS.put(id.toLowerCase(), provider);
	}

	@Override
	public IConfigKey addConfig(String group0, String id, IConfigValue value)
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
	}

	@Override
	public void addOptionalServerMod(String mod)
	{
		SharedServerData.INSTANCE.optionalServerMods.add(mod);
	}

	@Override
	public void addGuiContainer(ResourceLocation id, IContainerProvider provider)
	{
		GUI_CONTAINER_PROVIDERS.put(id, provider);
	}

	@Override
	public void addInfoTextLine(String id, IGuideTextLineProvider provider)
	{
		GuidePage.LINE_PROVIDERS.put(id.toLowerCase(), provider);
	}

	@Override
	public void addSyncData(String mod, ISyncData data)
	{
		SYNCED_DATA.put(mod, data);
	}

	@Override
	public void addUniverseDataProvider(ResourceLocation id, IDataProvider<IUniverse> provider)
	{
		DATA_PROVIDER_UNIVERSE.put(id, provider);
	}

	@Override
	public void addPlayerDataProvider(ResourceLocation id, IDataProvider<IForgePlayer> provider)
	{
		DATA_PROVIDER_PLAYER.put(id, provider);
	}

	@Override
	public void addTeamDataProvider(ResourceLocation id, IDataProvider<IForgeTeam> provider)
	{
		DATA_PROVIDER_TEAM.put(id, provider);
	}

	@Override
	public void addRankConfig(String id, IConfigValue defPlayer, IConfigValue defOP)
	{
		Preconditions.checkArgument(!RANK_CONFIGS.containsKey(id), "Duplicate RankConfig ID found: " + id);
		RankConfig c = new RankConfig(id, defPlayer, defOP);
		c.setNameLangKey("rank_config." + id + ".name");
		c.setInfoLangKey("rank_config." + id + ".info");
		RANK_CONFIGS.put(c.getName(), c);
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

		JsonElement overridesE = JsonUtils.fromJson(new File(CommonUtils.folderModpack, "overrides.json"));

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