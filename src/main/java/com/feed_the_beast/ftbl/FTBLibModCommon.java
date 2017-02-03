package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.IDataProvider;
import com.feed_the_beast.ftbl.api.IFTBLibPlugin;
import com.feed_the_beast.ftbl.api.IFTBLibRegistry;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.IRankConfig;
import com.feed_the_beast.ftbl.api.ISyncData;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigFile;
import com.feed_the_beast.ftbl.api.config.IConfigFileProvider;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.feed_the_beast.ftbl.api.gui.IContainerProvider;
import com.feed_the_beast.ftbl.api.info.IInfoTextLineProvider;
import com.feed_the_beast.ftbl.api_impl.LMRecipes;
import com.feed_the_beast.ftbl.api_impl.SharedServerData;
import com.feed_the_beast.ftbl.client.EnumNotificationDisplay;
import com.feed_the_beast.ftbl.lib.NBTDataStorage;
import com.feed_the_beast.ftbl.lib.config.ConfigFile;
import com.feed_the_beast.ftbl.lib.config.ConfigKey;
import com.feed_the_beast.ftbl.lib.config.PropertyBlockState;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.config.PropertyByte;
import com.feed_the_beast.ftbl.lib.config.PropertyColor;
import com.feed_the_beast.ftbl.lib.config.PropertyDouble;
import com.feed_the_beast.ftbl.lib.config.PropertyEntityClassList;
import com.feed_the_beast.ftbl.lib.config.PropertyEnumAbstract;
import com.feed_the_beast.ftbl.lib.config.PropertyInt;
import com.feed_the_beast.ftbl.lib.config.PropertyIntList;
import com.feed_the_beast.ftbl.lib.config.PropertyItemStackList;
import com.feed_the_beast.ftbl.lib.config.PropertyJson;
import com.feed_the_beast.ftbl.lib.config.PropertyNull;
import com.feed_the_beast.ftbl.lib.config.PropertyShort;
import com.feed_the_beast.ftbl.lib.config.PropertyString;
import com.feed_the_beast.ftbl.lib.config.PropertyStringEnum;
import com.feed_the_beast.ftbl.lib.config.PropertyStringList;
import com.feed_the_beast.ftbl.lib.config.PropertyTextComponentList;
import com.feed_the_beast.ftbl.lib.info.InfoExtendedTextLine;
import com.feed_the_beast.ftbl.lib.info.InfoHrLine;
import com.feed_the_beast.ftbl.lib.info.InfoImageLine;
import com.feed_the_beast.ftbl.lib.info.InfoListLine;
import com.feed_the_beast.ftbl.lib.info.InfoPage;
import com.feed_the_beast.ftbl.lib.info.InfoPageHelper;
import com.feed_the_beast.ftbl.lib.info.InfoTextLineString;
import com.feed_the_beast.ftbl.lib.info.ItemListLine;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.lib.internal.FTBLibNotifications;
import com.feed_the_beast.ftbl.lib.internal.FTBLibTeamPermissions;
import com.feed_the_beast.ftbl.lib.net.MessageLM;
import com.feed_the_beast.ftbl.lib.util.LMJsonUtils;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class FTBLibModCommon implements IFTBLibRegistry // FTBLibModClient
{
    public static final Map<String, IConfigValueProvider> CONFIG_VALUE_PROVIDERS = new HashMap<>();
    public static final Map<String, IConfigFile> CONFIG_FILES = new HashMap<>();
    public static final Map<UUID, IConfigContainer> TEMP_SERVER_CONFIG = new HashMap<>();
    public static final Map<ResourceLocation, IContainerProvider> GUI_CONTAINER_PROVIDERS = new HashMap<>();
    private static final Collection<String> TEAM_PLAYER_PERMISSIONS = new HashSet<>();
    public static final Collection<String> VISIBLE_TEAM_PLAYER_PERMISSIONS = new HashSet<>();
    public static final Map<String, ISyncData> SYNCED_DATA = new HashMap<>();
    public static final Map<ResourceLocation, IDataProvider<IUniverse>> DATA_PROVIDER_UNIVERSE = new HashMap<>();
    public static final Map<ResourceLocation, IDataProvider<IForgePlayer>> DATA_PROVIDER_PLAYER = new HashMap<>();
    public static final Map<ResourceLocation, IDataProvider<IForgeTeam>> DATA_PROVIDER_TEAM = new HashMap<>();
    private static final Map<String, IRankConfig> RANK_CONFIGS = new HashMap<>();
    public static final Map<String, IRankConfig> RANK_CONFIGS_MIRROR = Collections.unmodifiableMap(RANK_CONFIGS);

    private static class RankConfig extends ConfigKey implements IRankConfig
    {
        private final IConfigValue defaultOPValue;

        private RankConfig(String s, IConfigValue def, IConfigValue defOP, String... info)
        {
            super(s, def);
            defaultOPValue = def.copy();
            defaultOPValue.deserializeNBT(defOP.serializeNBT());
            setInfo(info);
        }

        @Override
        public IConfigValue getDefOPValue()
        {
            return defaultOPValue;
        }
    }

    public void preInit()
    {
        addOptionalServerMod(FTBLibFinals.MOD_ID);
        addConfigFileProvider(FTBLibFinals.MOD_ID, () -> new File(LMUtils.folderLocal, "ftbl.json"));

        addConfig(FTBLibFinals.MOD_ID, "teams.autocreate_on_login", FTBLibConfig.AUTOCREATE_TEAMS);
        addConfig(FTBLibFinals.MOD_ID, "teams.mirror_ftb_commands", FTBLibConfig.MIRROR_FTB_COMMANDS);

        addConfigValueProvider(PropertyNull.ID, () -> PropertyNull.INSTANCE);
        addConfigValueProvider(PropertyBool.ID, PropertyBool::new);
        addConfigValueProvider(PropertyByte.ID, PropertyByte::new);
        addConfigValueProvider(PropertyShort.ID, PropertyShort::new);
        addConfigValueProvider(PropertyInt.ID, PropertyInt::new);
        addConfigValueProvider(PropertyDouble.ID, PropertyDouble::new);
        addConfigValueProvider(PropertyString.ID, PropertyString::new);
        addConfigValueProvider(PropertyColor.ID, PropertyColor::new);
        addConfigValueProvider(PropertyEnumAbstract.ID, PropertyStringEnum::new);
        addConfigValueProvider(PropertyStringList.ID, PropertyStringList::new);
        addConfigValueProvider(PropertyIntList.ID, PropertyIntList::new);
        addConfigValueProvider(PropertyJson.ID, PropertyJson::new);
        addConfigValueProvider(PropertyBlockState.ID, PropertyBlockState::new);
        addConfigValueProvider(PropertyEntityClassList.ID, PropertyEntityClassList::new);
        addConfigValueProvider(PropertyTextComponentList.ID, PropertyTextComponentList::new);
        addConfigValueProvider(PropertyItemStackList.ID, PropertyItemStackList::new);

        addNotification(FTBLibNotifications.RELOAD_CLIENT_CONFIG);

        addInfoTextLine("img", (page, json) -> new InfoImageLine(json));
        addInfoTextLine("image", (page, json) -> new InfoImageLine(json));
        addInfoTextLine("text_component", (page, json) -> new InfoExtendedTextLine(json));
        addInfoTextLine("text", (page, json) -> new InfoTextLineString(json));
        addInfoTextLine("list", InfoListLine::new);
        addInfoTextLine("hr", (page, json) -> new InfoHrLine(json));
        addInfoTextLine("item_list", (page, json) -> new ItemListLine(json));

        addTeamPlayerPermission(FTBLibTeamPermissions.CAN_JOIN, true);
        addTeamPlayerPermission(FTBLibTeamPermissions.IS_ALLY, true);
        addTeamPlayerPermission(FTBLibTeamPermissions.IS_ENEMY, true);
        addTeamPlayerPermission(FTBLibTeamPermissions.EDIT_SETTINGS, false);
        addTeamPlayerPermission(FTBLibTeamPermissions.EDIT_PERMISSIONS, false);
        addTeamPlayerPermission(FTBLibTeamPermissions.MANAGE_MEMBERS, false);
        addTeamPlayerPermission(FTBLibTeamPermissions.MANAGE_ALLIES, false);
        addTeamPlayerPermission(FTBLibTeamPermissions.MANAGE_ENEMIES, false);

        for(IFTBLibPlugin plugin : FTBLibIntegrationInternal.API.getAllPlugins())
        {
            plugin.registerCommon(this);
        }
    }

    public void postInit()
    {
        reloadConfig();

        for(IFTBLibPlugin plugin : FTBLibIntegrationInternal.API.getAllPlugins())
        {
            plugin.registerRecipes(new LMRecipes());
        }
    }

    public void loadRegistries()
    {
    }

    public void spawnDust(World worldObj, double x, double y, double z, int i)
    {
    }

    @Override
    public void addConfigFileProvider(String id, IConfigFileProvider provider)
    {
        if(id.charAt(0) != '-')
        {
            id = id.toLowerCase();
            ConfigFile configFile = new ConfigFile(new TextComponentString(id), provider);
            CONFIG_FILES.put(id, configFile);
        }
    }

    @Override
    public void addConfigValueProvider(String id, IConfigValueProvider provider)
    {
        CONFIG_VALUE_PROVIDERS.put(id.toLowerCase(), provider);
    }

    @Override
    public void addConfig(String file, IConfigKey key, IConfigValue value)
    {
        IConfigFile configFile = CONFIG_FILES.get(file);

        if(configFile == null)
        {
            configFile = new ConfigFile(new TextComponentString(file), () -> null);
            CONFIG_FILES.put(file, configFile);
        }

        configFile.add(key, value);
    }

    @Override
    public void addOptionalServerMod(String mod)
    {
        SharedServerData.INSTANCE.optionalServerMods.add(mod);
    }

    @Override
    public void addNotification(INotification notification)
    {
        SharedServerData.INSTANCE.notifications.put(notification.getID(), notification);
    }

    @Override
    public void addGuiContainer(ResourceLocation id, IContainerProvider provider)
    {
        GUI_CONTAINER_PROVIDERS.put(id, provider);
    }

    @Override
    public void addInfoTextLine(String id, IInfoTextLineProvider provider)
    {
        InfoPageHelper.INFO_TEXT_LINE_PROVIDERS.put(id.toLowerCase(), provider);
    }

    @Override
    public void addTeamPlayerPermission(String permission, boolean visible)
    {
        TEAM_PLAYER_PERMISSIONS.add(permission);

        if(visible)
        {
            VISIBLE_TEAM_PLAYER_PERMISSIONS.add(permission);
        }
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
    public void addRankConfig(String id, IConfigValue defPlayer, IConfigValue defOP, String... description)
    {
        Preconditions.checkArgument(!RANK_CONFIGS.containsKey(id), "Duplicate RankConfig ID found: " + id);
        RankConfig c = new RankConfig(id, defPlayer, defOP);
        c.setInfo(description);
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

    public void reloadConfig()
    {
        loadAllFiles();
        JsonElement overridesE = LMJsonUtils.fromJson(new File(LMUtils.folderModpack, "overrides.json"));

        if(overridesE.isJsonObject())
        {
            overridesE.getAsJsonObject().entrySet().forEach(entry ->
            {
                if(entry.getValue().isJsonObject())
                {
                    IConfigFile file = CONFIG_FILES.get(entry.getKey());

                    if(file != null)
                    {
                        file.fromJson(entry.getValue());
                    }
                }
            });
        }

        saveAllFiles();
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
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        });

        return storage.isEmpty() ? null : storage;
    }

    public IConfigFile getClientConfig()
    {
        return null;
    }

    public void handleClientMessage(MessageLM<?> message)
    {
    }

    public void displayInfoGui(InfoPage page)
    {
    }

    public void displayNotification(EnumNotificationDisplay display, INotification n)
    {
    }
}