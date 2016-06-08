package com.feed_the_beast.ftbl.api.client.gui.guibuttons;

import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.config.ConfigEntry;
import com.feed_the_beast.ftbl.api.config.ConfigEntryBool;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 15.01.2016.
 */
public class ActionButtonRegistry
{
    public static final ConfigGroup configGroup = new ConfigGroup();
    private static final Map<ResourceLocation, ActionButton> map = new HashMap<>();

    public static ActionButton add(final ActionButton a)
    {
        if(a != null)
        {
            map.put(a.getResourceLocation(), a);

            if(a.configDefault != null)
            {
                ConfigEntryBool entry = new ConfigEntryBool(a.configDefault);
                entry.setDisplayName(a.displayName);
                configGroup.entryMap.put(a.getID(), entry);
            }
        }

        return a;
    }

    public static boolean enabled(ResourceLocation id)
    {
        ConfigEntry entry = configGroup.entryMap.get(id);
        return (entry == null) || entry.getAsBoolean();
    }

    public static ActionButton get(ResourceLocation key)
    {
        return map.get(key);
    }

    public static List<ActionButton> getButtons(ForgePlayerSP player, boolean ignoreConfig, boolean sort)
    {
        List<ActionButton> l = new ArrayList<>();

        for(ActionButton a : map.values())
        {
            if(a.isVisibleFor(player))
            {
                if(!ignoreConfig && a.configDefault != null)
                {
                    if(!enabled(a.getResourceLocation()))
                    {
                        continue;
                    }
                }

                l.add(a);
            }
        }

        if(sort)
        {
            Collections.sort(l);
        }

        return l;
    }
}