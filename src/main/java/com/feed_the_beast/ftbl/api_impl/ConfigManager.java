package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.IConfigManager;
import com.feed_the_beast.ftbl.api.config.ConfigFileProvider;
import com.feed_the_beast.ftbl.api.config.ConfigValue;
import com.feed_the_beast.ftbl.api.config.ConfigValueProvider;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigFile;
import com.feed_the_beast.ftbl.api.config.IConfigFileProvider;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.feed_the_beast.ftbl.api.events.ReloadType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.latmod.lib.config.ConfigFile;
import com.latmod.lib.config.ConfigKey;
import com.latmod.lib.config.ConfigTree;
import com.latmod.lib.reg.SimpleRegistry;
import com.latmod.lib.reg.StringIDRegistry;
import com.latmod.lib.reg.SyncedRegistry;
import com.latmod.lib.util.LMJsonUtils;
import com.latmod.lib.util.LMUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

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
 * Created by LatvianModder on 17.09.2016.
 */
public enum ConfigManager implements IConfigManager
{
    INSTANCE;

    private final SyncedRegistry<String, IConfigValueProvider> CONFIG_VALUES = new SyncedRegistry<>(new StringIDRegistry(), false);
    private final SimpleRegistry<String, IConfigFileProvider> FILE_PROVIDERS = new SimpleRegistry<>(false);
    private final IConfigTree CLIENT_CONFIG = new ConfigTree();
    private final ConfigFile CLIENT_CONFIG_FILE = new ConfigFile();
    private final IConfigTree CONFIG = new ConfigTree();
    private final Collection<IConfigFile> CONFIG_FILES = new ArrayList<>();
    private final Map<UUID, IConfigContainer> TEMP_SERVER_CONFIG = new HashMap<>();

    @Override
    public SyncedRegistry<String, IConfigValueProvider> configValues()
    {
        return CONFIG_VALUES;
    }

    @Override
    public SimpleRegistry<String, IConfigFileProvider> fileProviders()
    {
        return FILE_PROVIDERS;
    }

    @Override
    public IConfigTree clientConfig()
    {
        return CLIENT_CONFIG;
    }

    @Override
    public Collection<IConfigFile> registredFiles()
    {
        return CONFIG_FILES;
    }

    @Override
    public Map<UUID, IConfigContainer> getTempConfig()
    {
        return TEMP_SERVER_CONFIG;
    }

    public void init(ASMDataTable table)
    {
        LMUtils.findAnnotatedObjects(table, IConfigValueProvider.class, ConfigValueProvider.class, (obj, data) ->
        {
            String s = (String) data.getAnnotationInfo().get("value");

            if(s != null && !s.isEmpty())
            {
                CONFIG_VALUES.register(s.toLowerCase(Locale.ENGLISH), obj);
            }

            return null;
        });

        LMUtils.findAnnotatedObjects(table, IConfigFileProvider.class, ConfigFileProvider.class, (obj, data) ->
        {
            String s = (String) data.getAnnotationInfo().get("value");

            if(s != null && !s.isEmpty())
            {
                FILE_PROVIDERS.register(s.toLowerCase(Locale.ENGLISH), obj);
            }

            return null;
        });

        LMUtils.findAnnotatedObjects(table, IConfigValue.class, ConfigValue.class, (obj, data) ->
        {
            String id = (String) data.getAnnotationInfo().get("id");
            String file = (String) data.getAnnotationInfo().get("file");

            if(id != null && file != null && !id.isEmpty() && !file.isEmpty())
            {
                boolean client = data.getAnnotationInfo().containsKey("client") && (boolean) data.getAnnotationInfo().get("client");
                String displayName = data.getAnnotationInfo().containsKey("displayName") ? (String) data.getAnnotationInfo().get("displayName") : null;

                byte flags = 0;

                if(data.getAnnotationInfo().containsKey("isExcluded") && (boolean) data.getAnnotationInfo().get("isExcluded"))
                {
                    flags |= IConfigKey.EXCLUDED;
                }

                if(data.getAnnotationInfo().containsKey("isHidden") && (boolean) data.getAnnotationInfo().get("isHidden"))
                {
                    flags |= IConfigKey.HIDDEN;
                }

                if(data.getAnnotationInfo().containsKey("canEdit") && !(boolean) data.getAnnotationInfo().get("canEdit"))
                {
                    flags |= IConfigKey.CANT_EDIT;
                }

                if(data.getAnnotationInfo().containsKey("useScrollBar") && (boolean) data.getAnnotationInfo().get("useScrollBar"))
                {
                    flags |= IConfigKey.USE_SCROLL_BAR;
                }

                if(data.getAnnotationInfo().containsKey("translateDisplayName") && (boolean) data.getAnnotationInfo().get("translateDisplayName"))
                {
                    flags |= IConfigKey.TRANSLATE_DISPLAY_NAME;
                }

                ConfigKey key = new ConfigKey(file + "." + id, obj.copy(), displayName, false);
                key.setFlags(flags);

                Object infoObj = data.getAnnotationInfo().get("info");

                if(infoObj instanceof String)
                {
                    if(infoObj.toString().isEmpty())
                    {
                        key.setInfo(infoObj.toString());
                    }
                }
                else if(infoObj instanceof List<?>)
                {
                    if(((List<?>) infoObj).size() == 1)
                    {
                        key.setInfo(((List<?>) infoObj).get(0).toString());
                    }
                }

                if(client)
                {
                    CLIENT_CONFIG.add(key, obj);
                }
                else
                {
                    CONFIG.add(key, obj);
                }
            }

            return null;
        });

        CONFIG_VALUES.getIDs().generateIDs(CONFIG_VALUES.getKeys());

        LMUtils.DEV_LOGGER.info("Found IConfigValueProviders [" + CONFIG_VALUES.size() + "]: " + CONFIG_VALUES.getKeys());
        LMUtils.DEV_LOGGER.info("Found IConfigFileProviders [" + FILE_PROVIDERS.size() + "]: " + FILE_PROVIDERS.getKeys());
        LMUtils.DEV_LOGGER.info("Found ClientConfig IConfigValues [" + CLIENT_CONFIG.getTree().size() + "]: " + CLIENT_CONFIG.getTree().keySet());
        LMUtils.DEV_LOGGER.info("Found CommonConfig IConfigValues [" + CONFIG.getTree().size() + "]: " + CONFIG.getTree().keySet());
    }

    public final IConfigContainer CONFIG_CONTAINER = new IConfigContainer()
    {
        @Override
        public IConfigTree getTree()
        {
            return CONFIG;
        }

        @Override
        public ITextComponent getTitle()
        {
            return new TextComponentString("Server Config"); //TODO: Lang
        }

        @Override
        public void saveConfig(ICommandSender sender, @Nullable NBTTagCompound nbt, JsonObject json)
        {
            CONFIG.fromJson(json);
            CONFIG.getTree().values().stream().filter(value -> value instanceof IConfigFile).forEach(value -> ((IConfigFile) value).save());
            FTBLibAPI_Impl.INSTANCE.reload(sender, ReloadType.SERVER_ONLY);
        }
    };

    //new ResourceLocation(FTBLibFinals.MOD_ID, "client_config")
    public final IConfigContainer CLIENT_CONFIG_CONTAINER = new IConfigContainer()
    {
        @Override
        public IConfigTree getTree()
        {
            return CLIENT_CONFIG_FILE;
        }

        @Override
        public ITextComponent getTitle()
        {
            return new TextComponentString("client_settings"); //TODO: Lang
        }

        @Override
        public void saveConfig(ICommandSender sender, @Nullable NBTTagCompound nbt, JsonObject json)
        {
            CLIENT_CONFIG_FILE.fromJson(json);
            saveClientConfig();
        }
    };

    public void saveClientConfig()
    {
        if(CLIENT_CONFIG_FILE.getFile() == null)
        {
            CLIENT_CONFIG_FILE.setFile(new File(LMUtils.folderLocal, "client/config.json"));
            CLIENT_CONFIG_FILE.load();
        }

        CLIENT_CONFIG_FILE.save();
    }

    public void reloadConfig()
    {
        CONFIG.getTree().values().stream().filter(value -> value instanceof IConfigFile).forEach(value -> ((IConfigFile) value).load());

        LMUtils.DEV_LOGGER.info("Loading override configs");
        JsonElement overridesE = LMJsonUtils.fromJson(new File(LMUtils.folderModpack, "overrides.json"));

        if(overridesE.isJsonObject())
        {
            CONFIG_CONTAINER.getTree().fromJson(overridesE.getAsJsonObject());
        }

        CONFIG.getTree().values().stream().filter(value -> value instanceof IConfigFile).forEach(value -> ((IConfigFile) value).save());
    }
}