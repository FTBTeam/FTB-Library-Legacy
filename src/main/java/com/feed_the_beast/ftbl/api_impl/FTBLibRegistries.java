package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.ISyncData;
import com.feed_the_beast.ftbl.api.NotificationVariant;
import com.feed_the_beast.ftbl.api.OptionalServerModID;
import com.feed_the_beast.ftbl.api.SyncData;
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
import com.feed_the_beast.ftbl.api.gui.GuiHandler;
import com.feed_the_beast.ftbl.api.gui.IGuiHandler;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.feed_the_beast.ftbl.api.gui.SidebarButton;
import com.feed_the_beast.ftbl.api.recipes.IRecipeHandler;
import com.feed_the_beast.ftbl.api.recipes.RecipeHandler;
import com.feed_the_beast.ftbl.client.FTBLibActions;
import com.feed_the_beast.ftbl.lib.Notification;
import com.feed_the_beast.ftbl.lib.ResourceLocationComparator;
import com.feed_the_beast.ftbl.lib.config.ConfigFile;
import com.feed_the_beast.ftbl.lib.config.ConfigKey;
import com.feed_the_beast.ftbl.lib.reg.ResourceLocationIDRegistry;
import com.feed_the_beast.ftbl.lib.reg.StringIDRegistry;
import com.feed_the_beast.ftbl.lib.reg.SyncedRegistry;
import com.feed_the_beast.ftbl.lib.util.ASMUtils;
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
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
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

    public static final Comparator<ISidebarButton> SIDEBAR_BUTTON_COMPARATOR = (o1, o2) ->
    {
        int i = Integer.compare(o2.getPriority(), o1.getPriority());

        if(i == 0)
        {
            i = ResourceLocationComparator.INSTANCE.compare(o1.getID(), o2.getID());
        }

        return i;
    };

    public final SyncedRegistry<String, IConfigValueProvider> CONFIG_VALUES = new SyncedRegistry<>(new StringIDRegistry(), false);
    public final Map<String, IConfigFile> CONFIG_FILES = new HashMap<>();
    public final Map<UUID, IConfigContainer> TEMP_SERVER_CONFIG = new HashMap<>();
    public IConfigFile CLIENT_CONFIG;
    public final Map<ResourceLocation, ISyncData> SYNCED_DATA = new HashMap<>();
    public final SyncedRegistry<ResourceLocation, IGuiHandler> GUIS = new SyncedRegistry<>(new ResourceLocationIDRegistry(), true);
    public final Map<ResourceLocation, ISidebarButton> SIDEBAR_BUTTONS = new HashMap<>();
    public final SyncedRegistry<String, INotification> NOTIFICATIONS = new SyncedRegistry<>(new StringIDRegistry(), true);
    public final TShortObjectHashMap<INotification> CACHED_NOTIFICATIONS = new TShortObjectHashMap<>();
    public final Collection<IRecipeHandler> RECIPE_HANDLERS = new ArrayList<>();
    public final Collection<String> OPTIONAL_SERVER_MODS = new HashSet<>();
    public final Collection<String> OPTIONAL_SERVER_MODS_CLIENT = new HashSet<>();

    public void init(ASMDataTable table)
    {
        ASMUtils.findAnnotatedObjects(table, IConfigValueProvider.class, ConfigValueProvider.class, (obj, field, info) ->
        {
            String s = info.getString("value", "");

            if(!s.isEmpty())
            {
                CONFIG_VALUES.register(s.toLowerCase(Locale.ENGLISH), obj);
            }
        });

        ASMUtils.findAnnotatedObjects(table, IConfigFileProvider.class, ConfigFileProvider.class, (obj, field, info) ->
        {
            String s = info.getString("value", "");

            if(!s.isEmpty() && s.charAt(0) != '-')
            {
                s = s.toLowerCase(Locale.ENGLISH);
                ITextComponent n = new TextComponentString(s);
                ConfigFile configFile = new ConfigFile(n, obj);
                CONFIG_FILES.put(s, configFile);
            }
        });

        CLIENT_CONFIG = new ConfigFile(new TextComponentTranslation("Client Config"), () -> new File(LMUtils.folderLocal, "client_config.json")); //TODO: Lang
        CONFIG_FILES.put("client_config", CLIENT_CONFIG);

        int[] configValuesCount = {0};

        ASMUtils.findAnnotatedObjects(table, IConfigValue.class, ConfigValue.class, (obj, field, info) ->
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

                ConfigKey key = new ConfigKey(client ? (file + "." + id) : id, obj.copy(), displayName, false);
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

        CONFIG_VALUES.getIDs().generateIDs(CONFIG_VALUES.getMap().keySet());

        ASMUtils.findAnnotatedObjects(table, INotification.class, NotificationVariant.class, (obj, field, data) -> NOTIFICATIONS.register(obj.getID() + "@" + obj.getVariant(), obj));
        ASMUtils.findAnnotatedObjects(table, IGuiHandler.class, GuiHandler.class, (obj, field, data) -> GUIS.register(obj.getID(), obj));
        ASMUtils.findAnnotatedObjects(table, ISyncData.class, SyncData.class, (obj, field, data) -> SYNCED_DATA.put(obj.getID(), obj));
        ASMUtils.findAnnotatedObjects(table, ISidebarButton.class, SidebarButton.class, (obj, field, data) -> SIDEBAR_BUTTONS.put(obj.getID(), obj));
        ASMUtils.findAnnotatedObjects(table, IRecipeHandler.class, RecipeHandler.class, (obj, field, data) -> RECIPE_HANDLERS.add(obj));
        ASMUtils.findAnnotatedObjects(table, String.class, OptionalServerModID.class, (obj, field, data) -> OPTIONAL_SERVER_MODS.add(obj));

        GUIS.getIDs().generateIDs(GUIS.getMap().keySet());

        for(ISidebarButton button : SIDEBAR_BUTTONS.values())
        {
            IConfigValue value = button.getConfig();

            if(value != null)
            {
                CLIENT_CONFIG.add(new ConfigKey(button.getPath(), value.copy()), value);
            }
        }

        LMUtils.DEV_LOGGER.info("Found " + CONFIG_VALUES.getMap().size() + " IConfigValueProviders: " + CONFIG_VALUES.getMap().keySet());
        LMUtils.DEV_LOGGER.info("Found " + CONFIG_FILES.size() + " IConfigFiles: " + CONFIG_FILES.keySet());
        LMUtils.DEV_LOGGER.info("Found " + configValuesCount[0] + " IConfigValues, " + CLIENT_CONFIG.getTree().size() + " Client IConfigValues");
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

    public List<ISidebarButton> getSidebarButtons(boolean ignoreConfig)
    {
        List<ISidebarButton> l = new ArrayList<>();

        SIDEBAR_BUTTONS.forEach((key, value) ->
        {
            if(value.isVisible())
            {
                if(ignoreConfig || value.getConfig() == null || value.getConfig().getBoolean())
                {
                    l.add(value);
                }
            }
        });

        return l;
    }

    public void worldLoaded()
    {
        NOTIFICATIONS.getIDs().generateIDs(NOTIFICATIONS.getMap().keySet());
        CACHED_NOTIFICATIONS.clear();
        NOTIFICATIONS.getMap().forEach((key, value) -> CACHED_NOTIFICATIONS.put(NOTIFICATIONS.getIDs().getIDFromKey(key), Notification.copy(value)));
    }
}