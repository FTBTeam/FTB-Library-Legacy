package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.ISyncData;
import com.feed_the_beast.ftbl.api.NotificationVariant;
import com.feed_the_beast.ftbl.api.SyncData;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.gui.GuiHandler;
import com.feed_the_beast.ftbl.api.gui.IGuiHandler;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.feed_the_beast.ftbl.api.gui.SidebarButton;
import com.feed_the_beast.ftbl.api.recipes.IRecipeHandler;
import com.feed_the_beast.ftbl.api.recipes.RecipeHandler;
import com.latmod.lib.EnumEnabled;
import com.latmod.lib.ResourceLocationComparator;
import com.latmod.lib.config.ConfigKey;
import com.latmod.lib.config.PropertyBool;
import com.latmod.lib.config.SimpleConfigKey;
import com.latmod.lib.reg.ResourceLocationIDRegistry;
import com.latmod.lib.reg.SimpleRegistry;
import com.latmod.lib.reg.StringIDRegistry;
import com.latmod.lib.reg.SyncedRegistry;
import com.latmod.lib.util.LMUtils;
import gnu.trove.map.hash.TShortObjectHashMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public enum FTBLibRegistries
{
    INSTANCE;

    public final SimpleRegistry<ResourceLocation, ISyncData> SYNCED_DATA = new SimpleRegistry<>(false);
    public final SyncedRegistry<ResourceLocation, IGuiHandler> GUIS = new SyncedRegistry<>(new ResourceLocationIDRegistry(), true);
    public final SidebarButtonRegistry SIDEBAR_BUTTONS = new SidebarButtonRegistry();
    public final SyncedRegistry<String, INotification> NOTIFICATIONS = new SyncedRegistry<>(new StringIDRegistry(), true);
    public final TShortObjectHashMap<INotification> CACHED_NOTIFICATIONS = new TShortObjectHashMap<>();
    public final List<IRecipeHandler> RECIPE_HANDLERS = new ArrayList<>();

    public void init(ASMDataTable table)
    {
        LMUtils.findAnnotatedObjects(table, INotification.class, NotificationVariant.class, (obj, data) ->
        {
            NOTIFICATIONS.register(obj.getID() + "@" + obj.getVariant(), obj);
            return null;
        });

        NOTIFICATIONS.getIDs().generateIDs(NOTIFICATIONS.getKeys());

        LMUtils.findAnnotatedObjects(table, IGuiHandler.class, GuiHandler.class, (obj, data) ->
        {
            GUIS.register(obj.getID(), obj);
            return null;
        });

        GUIS.getIDs().generateIDs(GUIS.getKeys());

        LMUtils.findAnnotatedObjects(table, ISyncData.class, SyncData.class, (obj, data) ->
        {
            SYNCED_DATA.register(obj.getID(), obj);
            return null;
        });

        LMUtils.findAnnotatedObjects(table, ISidebarButton.class, SidebarButton.class, (obj, data) ->
        {
            SIDEBAR_BUTTONS.register(obj.getID(), obj);
            return null;
        });

        LMUtils.findAnnotatedObjects(table, IRecipeHandler.class, RecipeHandler.class, (obj, data) ->
        {
            RECIPE_HANDLERS.add(obj);
            return null;
        });
    }

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
                ConfigManager.INSTANCE.clientConfig().add(new ConfigKey(key.toString(), configValue), configValue.copy());
            }

            return a;
        }

        public boolean enabled(ResourceLocation id)
        {
            IConfigKey key = new SimpleConfigKey(id.toString());
            return !ConfigManager.INSTANCE.clientConfig().has(key) || ConfigManager.INSTANCE.clientConfig().get(key).getBoolean();
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
}