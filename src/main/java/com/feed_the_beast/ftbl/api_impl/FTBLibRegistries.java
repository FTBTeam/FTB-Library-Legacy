package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.IFTBLibRegistries;
import com.feed_the_beast.ftbl.api.IIntIDRegistry;
import com.feed_the_beast.ftbl.api.IRegistry;
import com.feed_the_beast.ftbl.api.ISyncData;
import com.feed_the_beast.ftbl.api.ISyncedRegistry;
import com.feed_the_beast.ftbl.api.config.ConfigEntry;
import com.feed_the_beast.ftbl.api.config.ConfigEntryBool;
import com.feed_the_beast.ftbl.api.config.ConfigFile;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
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

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
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
        private ConfigGroup sidebarButtonConfig = new ConfigGroup();

        private SidebarButtonRegistry()
        {
            super(true);
        }

        public ConfigGroup getSidebarButtonConfig()
        {
            return sidebarButtonConfig;
        }

        @Override
        public ISidebarButton register(ResourceLocation key, ISidebarButton a)
        {
            a = super.register(key, a);

            if(a.getConfigDefault() != null)
            {
                ConfigEntryBool entry = new ConfigEntryBool(a.getConfigDefault() == EnumEnabled.ENABLED);
                //entry.setDisplayName(a.displayName);
                sidebarButtonConfig.entryMap.put(key.toString(), entry);
            }

            return a;
        }

        public boolean enabled(ResourceLocation id)
        {
            ConfigEntry entry = sidebarButtonConfig.entryMap.get(id.toString());
            return (entry == null) || entry.getAsBoolean();
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

    public final Map<UUID, IConfigContainer> tempServerConfig = new HashMap<>();
    private final IRegistry<ResourceLocation, ISyncData> syncedData = new SimpleRegistry<>(false);
    private final SyncedRegistry<IGuiHandler> guis = new SyncedRegistry<>(true);
    private final SidebarButtonRegistry sidebarButtons = new SidebarButtonRegistry();
    private final IRegistry<String, ConfigFile> config = new SimpleRegistry<>(false);
    private final IntIDRegistry notifications = new IntIDRegistry();
    private final IRegistry<ResourceLocation, IRecipeHandler> recipeHandlers = new SimpleRegistry<>(true);
    private final Collection<ITickable> tickables = new ArrayList<>();
    //TODO: Make this Thread-safe
    private final List<ServerTickCallback> callbacks = new ArrayList<>();
    private final List<ServerTickCallback> pendingCallbacks = new ArrayList<>();

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
        public ConfigGroup createGroup()
        {
            ConfigGroup group = new ConfigGroup();

            for(Map.Entry<String, ConfigFile> c : config.getEntrySet())
            {
                group.add(c.getKey(), c.getValue());
            }

            return group;
        }

        @Override
        public ITextComponent getConfigTitle()
        {
            return new TextComponentString("Server Config"); //TODO: Lang
        }

        @Override
        public void saveConfig(ICommandSender sender, @Nullable NBTTagCompound nbt, JsonObject json)
        {
            createGroup().loadFromGroup(json);
            config.getValues().forEach(ConfigFile::save);
            FTBLibAPI.get().reload(sender, ReloadType.SERVER_ONLY);
        }
    };

    private FTBLibRegistries()
    {
    }

    @Override
    public IRegistry<ResourceLocation, ISyncData> syncedData()
    {
        return syncedData;
    }

    @Override
    public ISyncedRegistry<IGuiHandler> guis()
    {
        return guis;
    }

    @Override
    public SidebarButtonRegistry sidebarButtons()
    {
        return sidebarButtons;
    }

    @Override
    public IRegistry<String, ConfigFile> configFiles()
    {
        return config;
    }

    @Override
    public IIntIDRegistry notifications()
    {
        return notifications;
    }

    @Override
    public IRegistry<ResourceLocation, IRecipeHandler> recipeHandlers()
    {
        return recipeHandlers;
    }

    @Override
    public Collection<ITickable> tickables()
    {
        return tickables;
    }

    void addServerCallback(int timer, Runnable runnable)
    {
        if(timer <= 0)
        {
            runnable.run();
        }
        else
        {
            pendingCallbacks.add(new ServerTickCallback(timer, runnable));
        }
    }

    @Override
    public void update()
    {
        if(!tickables.isEmpty())
        {
            for(ITickable t : tickables)
            {
                t.update();
            }
        }

        if(!pendingCallbacks.isEmpty())
        {
            callbacks.addAll(pendingCallbacks);
            pendingCallbacks.clear();
        }

        if(!callbacks.isEmpty())
        {
            for(int i = callbacks.size() - 1; i >= 0; i--)
            {
                if(callbacks.get(i).incAndCheck())
                {
                    callbacks.remove(i);
                }
            }
        }
    }

    public void reloadConfig()
    {
        config.getValues().forEach(ConfigFile::load);

        LMUtils.DEV_LOGGER.info("Loading override configs");
        JsonElement overridesE = LMJsonUtils.fromJson(new File(LMUtils.folderModpack, "overrides.json"));

        if(overridesE.isJsonObject())
        {
            int result = CONFIG_CONTAINER.createGroup().loadFromGroup(overridesE.getAsJsonObject());

            if(result > 0)
            {
                LMUtils.DEV_LOGGER.info("Loaded " + result + " config overrides");

                for(ConfigFile f : config.getValues())
                {
                    f.save();
                }
            }
        }

        config.getValues().forEach(ConfigFile::save);
    }
}