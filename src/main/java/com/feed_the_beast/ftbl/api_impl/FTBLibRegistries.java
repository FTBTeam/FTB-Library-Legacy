package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.IFTBLibRegistries;
import com.feed_the_beast.ftbl.api.IIntIDRegistry;
import com.feed_the_beast.ftbl.api.IRegistry;
import com.feed_the_beast.ftbl.api.ISyncData;
import com.feed_the_beast.ftbl.api.ISyncedRegistry;
import com.feed_the_beast.ftbl.api.config.ConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigFile;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.SimpleConfigKey;
import com.feed_the_beast.ftbl.api.config.impl.ConfigFile;
import com.feed_the_beast.ftbl.api.config.impl.ConfigTree;
import com.feed_the_beast.ftbl.api.config.impl.PropertyBool;
import com.feed_the_beast.ftbl.api.config.impl.PropertyDouble;
import com.feed_the_beast.ftbl.api.config.impl.PropertyEnumAbstract;
import com.feed_the_beast.ftbl.api.config.impl.PropertyInt;
import com.feed_the_beast.ftbl.api.config.impl.PropertyNull;
import com.feed_the_beast.ftbl.api.config.impl.PropertyString;
import com.feed_the_beast.ftbl.api.config.impl.PropertyStringEnum;
import com.feed_the_beast.ftbl.api.config.impl.PropertyStringList;
import com.feed_the_beast.ftbl.api.events.ReloadType;
import com.feed_the_beast.ftbl.api.gui.IGuiHandler;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.feed_the_beast.ftbl.api.recipes.IRecipeHandler;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.latmod.lib.EnumEnabled;
import com.latmod.lib.ResourceLocationComparator;
import com.latmod.lib.util.LMJsonUtils;
import com.latmod.lib.util.LMUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public class FTBLibRegistries implements IFTBLibRegistries, ITickable
{
    static final FTBLibRegistries INSTANCE = new FTBLibRegistries();

    public static class SidebarButtonRegistry extends SimpleRegistry<ResourceLocation, ISidebarButton>
    {
        public static final Comparator<Map.Entry<ResourceLocation, ISidebarButton>> COMPARATOR = (o1, o2) ->
        {
            int i = Integer.compare(o2.getValue().getPriority(), o1.getValue().getPriority());

            if(i == 0)
            {
                i = ResourceLocationComparator.INSTANCE.compare(o1.getKey(), o2.getKey());
            }

            return i;
        };

        private SidebarButtonRegistry()
        {
            super(true);
        }

        @Override
        public ISidebarButton register(ResourceLocation key, ISidebarButton a)
        {
            a = super.register(key, a);

            if(a.getConfigDefault() != null)
            {
                //entry.setDisplayName(a.displayName);
                FTBLibAPI.get().getRegistries().clientConfig().add(new ConfigKey(key.toString(), new PropertyBool(a.getConfigDefault() == EnumEnabled.ENABLED), null));
            }

            return a;
        }

        public boolean enabled(ResourceLocation id)
        {
            IConfigKey key = new SimpleConfigKey(id.toString());
            return !FTBLibAPI.get().getRegistries().clientConfig().has(key) || FTBLibAPI.get().getRegistries().clientConfig().get(key).getBoolean();
        }

        public List<Map.Entry<ResourceLocation, ISidebarButton>> getButtons(boolean ignoreConfig)
        {
            List<Map.Entry<ResourceLocation, ISidebarButton>> l = new ArrayList<>();

            for(Map.Entry<ResourceLocation, ISidebarButton> entry : getEntrySet())
            {
                if(entry.getValue().isVisible())
                {
                    if(!ignoreConfig && entry.getValue().getConfigDefault() != null)
                    {
                        if(!enabled(entry.getKey()))
                        {
                            continue;
                        }
                    }

                    l.add(entry);
                }
            }

            return l;
        }
    }

    public static final Map<UUID, IConfigContainer> TEMP_SERVER_CONFIG = new HashMap<>();
    private static final IRegistry<ResourceLocation, ISyncData> SYNCED_DATA = new SimpleRegistry<>(false);
    private static final SyncedRegistry<IGuiHandler> GUIS = new SyncedRegistry<>(true);
    private static final SidebarButtonRegistry SIDEBAR_BUTTONS = new SidebarButtonRegistry();
    private static final IConfigTree CONFIG = new ConfigTree();
    private static final Collection<IConfigFile> CONFIG_FILES = new ArrayList<>();
    private static final IntIDRegistry NOTIFICATIONS = new IntIDRegistry();
    private static final IRegistry<ResourceLocation, IRecipeHandler> RECIPE_HANDLERS = new SimpleRegistry<>(true);
    private static final Collection<ITickable> TICKABLES = new ArrayList<>();
    private static final SyncedRegistry<IConfigValue> CONFIG_VALUES = new SyncedRegistry<>(false);
    private static final IConfigTree CLIENT_CONFIG = new ConfigTree();

    //TODO: Make this Thread-safe
    private static final List<ServerTickCallback> CALLBACKS = new ArrayList<>();
    private static final List<ServerTickCallback> PENDING_CALLBACKS = new ArrayList<>();
    private static final ConfigFile CLIENT_CONFIG_FILE = new ConfigFile();

    private class ServerTickCallback
    {
        private final int maxTick;
        private Runnable runnable;
        private int ticks = 0;

        private ServerTickCallback(int i, Runnable r)
        {
            maxTick = i;
            runnable = r;
        }

        private boolean incAndCheck()
        {
            ticks++;
            if(ticks >= maxTick)
            {
                runnable.run();
                return true;
            }

            return false;
        }
    }

    public final IConfigContainer CONFIG_CONTAINER = new IConfigContainer()
    {
        @Override
        public IConfigTree createGroup()
        {
            return CONFIG;
        }

        @Override
        public ITextComponent getConfigTitle()
        {
            return new TextComponentString("Server Config"); //TODO: Lang
        }

        @Override
        public void saveConfig(ICommandSender sender, @Nullable NBTTagCompound nbt, JsonObject json)
        {
            CONFIG.fromJson(json);
            CONFIG.getTree().values().stream().filter(value -> value instanceof IConfigFile).forEach(value -> ((IConfigFile) value).save());
            FTBLibAPI.get().reload(sender, ReloadType.SERVER_ONLY);
        }
    };

    //new ResourceLocation(FTBLibFinals.MOD_ID, "client_config")
    public final IConfigContainer CLIENT_CONFIG_CONTAINER = new IConfigContainer()
    {
        @Override
        public IConfigTree createGroup()
        {
            return CLIENT_CONFIG_FILE;
        }

        @Override
        public ITextComponent getConfigTitle()
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

    private FTBLibRegistries()
    {
        registerSync("guis", GUIS);
        registerSync("notifications", NOTIFICATIONS);

        CONFIG_VALUES.register(PropertyNull.ID, PropertyNull.INSTANCE);
        CONFIG_VALUES.register(ConfigTree.ID, new ConfigTree());
        CONFIG_VALUES.register(PropertyBool.ID, new PropertyBool(false));
        CONFIG_VALUES.register(PropertyString.ID, new PropertyString(""));
        CONFIG_VALUES.register(PropertyInt.ID, new PropertyInt(0));
        CONFIG_VALUES.register(PropertyDouble.ID, new PropertyDouble(0D));
        CONFIG_VALUES.register(PropertyStringList.ID, new PropertyStringList(Collections.emptyList()));
        CONFIG_VALUES.register(PropertyEnumAbstract.ID, new PropertyStringEnum(Collections.emptyList(), ""));
    }

    private void registerSync(String id, ISyncData data)
    {
        SYNCED_DATA.register(new ResourceLocation(FTBLibFinals.MOD_ID, id), data);
    }

    @Override
    public IRegistry<ResourceLocation, ISyncData> syncedData()
    {
        return SYNCED_DATA;
    }

    @Override
    public ISyncedRegistry<IGuiHandler> guis()
    {
        return GUIS;
    }

    @Override
    public SidebarButtonRegistry sidebarButtons()
    {
        return SIDEBAR_BUTTONS;
    }

    @Override
    public void registerConfigFile(String id, IConfigFile file, @Nonnull ITextComponent displayName)
    {

    }

    @Override
    public Collection<IConfigFile> getRegistredConfigFiles()
    {
        return CONFIG_FILES;
    }

    @Override
    public IIntIDRegistry notifications()
    {
        return NOTIFICATIONS;
    }

    @Override
    public IRegistry<ResourceLocation, IRecipeHandler> recipeHandlers()
    {
        return RECIPE_HANDLERS;
    }

    @Override
    public Collection<ITickable> tickables()
    {
        return TICKABLES;
    }

    @Override
    public ISyncedRegistry<IConfigValue> configValues()
    {
        return CONFIG_VALUES;
    }

    void addServerCallback(int timer, Runnable runnable)
    {
        if(timer <= 0)
        {
            runnable.run();
        }
        else
        {
            PENDING_CALLBACKS.add(new ServerTickCallback(timer, runnable));
        }
    }

    @Override
    public IConfigTree clientConfig()
    {
        return CLIENT_CONFIG;
    }

    @Override
    public void update()
    {
        if(!TICKABLES.isEmpty())
        {
            TICKABLES.forEach(ITickable::update);
        }

        if(!PENDING_CALLBACKS.isEmpty())
        {
            CALLBACKS.addAll(PENDING_CALLBACKS);
            PENDING_CALLBACKS.clear();
        }

        if(!CALLBACKS.isEmpty())
        {
            for(int i = CALLBACKS.size() - 1; i >= 0; i--)
            {
                if(CALLBACKS.get(i).incAndCheck())
                {
                    CALLBACKS.remove(i);
                }
            }
        }
    }

    public void reloadConfig()
    {
        CONFIG.getTree().values().stream().filter(value -> value instanceof IConfigFile).forEach(value -> ((IConfigFile) value).load());

        LMUtils.DEV_LOGGER.info("Loading override configs");
        JsonElement overridesE = LMJsonUtils.fromJson(new File(LMUtils.folderModpack, "overrides.json"));

        if(overridesE.isJsonObject())
        {
            CONFIG_CONTAINER.createGroup().fromJson(overridesE.getAsJsonObject());
        }

        CONFIG.getTree().values().stream().filter(value -> value instanceof IConfigFile).forEach(value -> ((IConfigFile) value).save());
    }
}