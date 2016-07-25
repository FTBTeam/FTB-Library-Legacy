package com.feed_the_beast.ftbl.api.client.gui.guibuttons;

import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.RegistryBase;
import com.feed_the_beast.ftbl.api.config.ConfigEntry;
import com.feed_the_beast.ftbl.api.config.ConfigEntryBool;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;
import com.feed_the_beast.ftbl.util.FTBLib;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 15.01.2016.
 */
public class ActionButtonRegistry
{
    public static final ConfigGroup configGroup = new ConfigGroup();
    public static final RegistryBase<ResourceLocation, ActionButton> INSTANCE = new RegistryBase<>(RegistryBase.LINKED | RegistryBase.ALLOW_OVERRIDES);
    public static final Comparator<Map.Entry<ResourceLocation, ActionButton>> COMPARATOR = (o1, o2) ->
    {
        int i = Integer.compare(o2.getValue().priority, o1.getValue().priority);

        if(i == 0)
        {
            i = FTBLib.RESOURCE_LOCATION_COMPARATOR.compare(o1.getKey(), o2.getKey());
        }

        return i;
    };

    public static ActionButton add(@Nonnull ResourceLocation key, @Nonnull ActionButton a)
    {
        a = INSTANCE.register(key, a);

        if(a.configDefault != null)
        {
            ConfigEntryBool entry = new ConfigEntryBool(a.configDefault);
            //entry.setDisplayName(a.displayName);
            configGroup.entryMap.put(key.toString(), entry);
        }

        return a;
    }

    public static boolean enabled(ResourceLocation id)
    {
        ConfigEntry entry = configGroup.entryMap.get(id.toString());
        return (entry == null) || entry.getAsBoolean();
    }

    public static List<Map.Entry<ResourceLocation, ActionButton>> getButtons(ForgePlayerSP player, boolean ignoreConfig)
    {
        List<Map.Entry<ResourceLocation, ActionButton>> l = new ArrayList<>();

        for(Map.Entry<ResourceLocation, ActionButton> a : INSTANCE.getEntrySet())
        {
            if(a.getValue().isVisibleFor(player))
            {
                if(!ignoreConfig && a.getValue().configDefault != null)
                {
                    if(!enabled(a.getKey()))
                    {
                        continue;
                    }
                }

                l.add(a);
            }
        }

        return l;
    }
}