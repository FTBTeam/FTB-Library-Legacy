package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.ISyncData;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.api.OptionalServerModID;
import com.feed_the_beast.ftbl.api.config.ConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigFile;
import com.feed_the_beast.ftbl.api.config.IConfigFileProvider;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.feed_the_beast.ftbl.api.events.player.IPlayerDataProvider;
import com.feed_the_beast.ftbl.api.events.team.ITeamDataProvider;
import com.feed_the_beast.ftbl.api.events.universe.IUniverseDataProvider;
import com.feed_the_beast.ftbl.api.gui.IGuiHandler;
import com.feed_the_beast.ftbl.api.info.IInfoTextLineProvider;
import com.feed_the_beast.ftbl.client.FTBLibActions;
import com.feed_the_beast.ftbl.lib.AsmData;
import com.feed_the_beast.ftbl.lib.NBTDataStorage;
import com.feed_the_beast.ftbl.lib.Notification;
import com.feed_the_beast.ftbl.lib.config.ConfigFile;
import com.feed_the_beast.ftbl.lib.config.ConfigKey;
import com.feed_the_beast.ftbl.lib.info.InfoPageHelper;
import com.feed_the_beast.ftbl.lib.util.LMJsonUtils;
import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import gnu.trove.map.hash.TShortObjectHashMap;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public enum FTBLibRegistries
{
    INSTANCE;

    public final Map<String, IConfigValueProvider> CONFIG_VALUES = new HashMap<>();
    public final Map<String, IConfigFile> CONFIG_FILES = new HashMap<>();
    public final Map<UUID, IConfigContainer> TEMP_SERVER_CONFIG = new HashMap<>();
    public final Map<ResourceLocation, ISyncData> SYNCED_DATA = new HashMap<>();
    public final Map<ResourceLocation, IGuiHandler> GUIS = new HashMap<>();
    public IConfigFile CLIENT_CONFIG;
    public final Map<String, INotification> NOTIFICATIONS = new HashMap<>();
    public final TShortObjectHashMap<INotification> CACHED_NOTIFICATIONS = new TShortObjectHashMap<>();
    private final Collection<IUniverseDataProvider> DATA_PROVIDER_UNIVERSE = new ArrayList<>();
    private final Collection<IPlayerDataProvider> DATA_PROVIDER_PLAYER = new ArrayList<>();
    private final Collection<ITeamDataProvider> DATA_PROVIDER_TEAM = new ArrayList<>();

    public void init(AsmData asmData)
    {
        asmData.findRegistryObjects(IConfigValueProvider.class, true, (obj, field, id) -> CONFIG_VALUES.put(id.toLowerCase(Locale.ENGLISH), obj));

        asmData.findRegistryObjects(IConfigFileProvider.class, true, (obj, field, id) ->
        {
            if(id.charAt(0) != '-')
            {
                id = id.toLowerCase(Locale.ENGLISH);
                ITextComponent n = new TextComponentString(id);
                ConfigFile configFile = new ConfigFile(n, obj);
                CONFIG_FILES.put(id, configFile);
            }
        });

        CLIENT_CONFIG = new ConfigFile(new TextComponentTranslation("sidebar_button.ftbl.settings"), () -> new File(LMUtils.folderLocal, "client_config.json"));
        CONFIG_FILES.put("client_config", CLIENT_CONFIG);

        int[] configValuesCount = {0};

        asmData.findAnnotatedObjects(IConfigValue.class, ConfigValue.class, (obj, field, info) ->
        {
            String id = info.getString("id", "");
            String file = info.getString("file", "");

            if(!id.isEmpty() && !file.isEmpty())
            {
                boolean client = info.getBoolean("client", false);
                String displayName = info.getString("displayName", "");

                byte flags = 0;

                if(info.getBoolean("isExcluded", false))
                {
                    flags |= IConfigKey.EXCLUDED;
                }

                if(info.getBoolean("isHidden", false))
                {
                    flags |= IConfigKey.HIDDEN;
                }

                if(!info.getBoolean("canEdit", true))
                {
                    flags |= IConfigKey.CANT_EDIT;
                }

                if(info.getBoolean("useScrollBar", false))
                {
                    flags |= IConfigKey.USE_SCROLL_BAR;
                }

                if(info.getBoolean("translateDisplayName", false))
                {
                    flags |= IConfigKey.TRANSLATE_DISPLAY_NAME;
                }

                ConfigKey key = new ConfigKey(client ? (file + '.' + id) : id, obj.copy(), displayName, false);
                key.setFlags(flags);

                List<String> keyInfo = info.getStringList("info");

                if(!keyInfo.isEmpty())
                {
                    key.setInfo(LMStringUtils.unsplit(keyInfo.toArray(new String[keyInfo.size()]), "\n"));
                }

                if(client)
                {
                    CLIENT_CONFIG.add(key, obj);
                }
                else
                {
                    IConfigFile configFile = CONFIG_FILES.get(file);

                    if(configFile == null)
                    {
                        configFile = new ConfigFile(new TextComponentString(file), () -> null);
                        CONFIG_FILES.put(file, configFile);
                    }

                    configFile.add(key, obj);
                    configValuesCount[0]++;
                }
            }
        });

        SharedData.SERVER.getConfigIDs().generateIDs(CONFIG_VALUES.keySet());

        asmData.findAnnotatedObjects(String.class, OptionalServerModID.class, (obj, field, info) -> SharedData.SERVER.optionalServerMods.add(obj));
        asmData.findRegistryObjects(INotification.class, false, (obj, field, id) -> NOTIFICATIONS.put(obj.getID() + "@" + obj.getVariant(), obj));
        asmData.findRegistryObjects(IGuiHandler.class, false, (obj, field, id) -> GUIS.put(obj.getID(), obj));
        asmData.findRegistryObjects(ISyncData.class, false, (obj, field, id) -> SYNCED_DATA.put(obj.getID(), obj));
        asmData.findRegistryObjects(IInfoTextLineProvider.class, true, (obj, field, id) -> InfoPageHelper.INFO_TEXT_LINE_PROVIDERS.put(id.toLowerCase(Locale.ENGLISH), obj));
        asmData.findRegistryObjects(IUniverseDataProvider.class, false, (obj, field, info) -> DATA_PROVIDER_UNIVERSE.add(obj));
        asmData.findRegistryObjects(IPlayerDataProvider.class, false, (obj, field, info) -> DATA_PROVIDER_PLAYER.add(obj));
        asmData.findRegistryObjects(ITeamDataProvider.class, false, (obj, field, info) -> DATA_PROVIDER_TEAM.add(obj));

        SharedData.SERVER.guiIDs.generateIDs(GUIS.keySet());

        LMUtils.DEV_LOGGER.info("Found " + CONFIG_VALUES.size() + " IConfigValueProviders: " + CONFIG_VALUES.keySet());
        LMUtils.DEV_LOGGER.info("Found " + CONFIG_FILES.size() + " IConfigFiles: " + CONFIG_FILES.keySet());
        LMUtils.DEV_LOGGER.info("Found " + configValuesCount[0] + " IConfigValues, " + CLIENT_CONFIG.getTree().size() + " Client IConfigValues");

        FTBLibMod.PROXY.loadRegistries(asmData);
    }

    public final IConfigContainer CLIENT_CONFIG_CONTAINER = new IConfigContainer()
    {
        private final ITextComponent TITLE = new TextComponentTranslation(FTBLibActions.SETTINGS.getPath());

        @Override
        public IConfigTree getConfigTree()
        {
            return CLIENT_CONFIG;
        }

        @Override
        public ITextComponent getTitle()
        {
            return TITLE;
        }

        @Override
        public void saveConfig(ICommandSender sender, @Nullable NBTTagCompound nbt, JsonObject json)
        {
            CLIENT_CONFIG.fromJson(json);
            CLIENT_CONFIG.save();
        }
    };

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

                    if(file != null && file != CLIENT_CONFIG)
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
        SharedData.SERVER.notificationIDs.generateIDs(NOTIFICATIONS.keySet());
        CACHED_NOTIFICATIONS.clear();
        NOTIFICATIONS.forEach((key, value) -> CACHED_NOTIFICATIONS.put(SharedData.SERVER.notificationIDs.getIDFromKey(key), Notification.copy(value)));
    }

    @Nullable
    public NBTDataStorage createUniverseDataStorage(IUniverse universe)
    {
        NBTDataStorage storage = new NBTDataStorage();

        for(IUniverseDataProvider provider : DATA_PROVIDER_UNIVERSE)
        {
            try
            {
                storage.add(provider.getUniverseData(universe));
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }

        return storage.isEmpty() ? null : storage;
    }

    @Nullable
    public NBTDataStorage createPlayerDataStorage(IForgePlayer player)
    {
        NBTDataStorage storage = new NBTDataStorage();

        for(IPlayerDataProvider provider : DATA_PROVIDER_PLAYER)
        {
            try
            {
                storage.add(provider.getPlayerData(player));
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }

        return storage.isEmpty() ? null : storage;
    }

    @Nullable
    public NBTDataStorage createTeamDataStorage(IForgeTeam team)
    {
        NBTDataStorage storage = new NBTDataStorage();

        for(ITeamDataProvider provider : DATA_PROVIDER_TEAM)
        {
            try
            {
                storage.add(provider.getTeamData(team));
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }

        return storage.isEmpty() ? null : storage;
    }
}