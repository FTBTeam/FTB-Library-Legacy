package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.IFTBLibRegistries;
import com.feed_the_beast.ftbl.api.IIntIDRegistry;
import com.feed_the_beast.ftbl.api.IRegistry;
import com.feed_the_beast.ftbl.api.ISyncData;
import com.feed_the_beast.ftbl.api.ISyncedRegistry;
import com.feed_the_beast.ftbl.api.config.IConfigFile;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.gui.IGuiHandler;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.feed_the_beast.ftbl.api.recipes.IRecipeHandler;
import com.feed_the_beast.ftbl.api_impl.config.ConfigKey;
import com.feed_the_beast.ftbl.api_impl.config.PropertyBool;
import com.feed_the_beast.ftbl.api_impl.config.SimpleConfigKey;
import com.latmod.lib.EnumEnabled;
import com.latmod.lib.ResourceLocationComparator;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public enum FTBLibRegistries implements IFTBLibRegistries, ITickable
{
    INSTANCE;

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
                PropertyBool configValue = new PropertyBool(a.getConfigDefault() == EnumEnabled.ENABLED);
                FTBLibAPI.get().configManager().clientConfig().add(new ConfigKey(key.toString(), configValue), configValue.copy());
            }

            return a;
        }

        public boolean enabled(ResourceLocation id)
        {
            IConfigKey key = new SimpleConfigKey(id.toString());
            return !FTBLibAPI.get().configManager().clientConfig().has(key) || FTBLibAPI.get().configManager().clientConfig().get(key).getBoolean();
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

    private final IRegistry<ResourceLocation, ISyncData> SYNCED_DATA = new SimpleRegistry<>(false);
    private final SyncedRegistry<ResourceLocation, IGuiHandler> GUIS = new SyncedRegistry<>(new ResourceLocationIntIDRegistry(), true);
    private final SidebarButtonRegistry SIDEBAR_BUTTONS = new SidebarButtonRegistry();
    private final IIntIDRegistry<ResourceLocation> NOTIFICATIONS = new ResourceLocationIntIDRegistry();
    private final IRegistry<ResourceLocation, IRecipeHandler> RECIPE_HANDLERS = new SimpleRegistry<>(true);
    private final Collection<ITickable> TICKABLES = new ArrayList<>();

    //TODO: Make this Thread-safe
    private final List<ServerTickCallback> CALLBACKS = new ArrayList<>();
    private final List<ServerTickCallback> PENDING_CALLBACKS = new ArrayList<>();

    private static class ServerTickCallback
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

    FTBLibRegistries()
    {
        registerSync("guis", GUIS);
        registerSync("notifications", NOTIFICATIONS);
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
    public ISyncedRegistry<ResourceLocation, IGuiHandler> guis()
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
    public IIntIDRegistry<ResourceLocation> notifications()
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
}