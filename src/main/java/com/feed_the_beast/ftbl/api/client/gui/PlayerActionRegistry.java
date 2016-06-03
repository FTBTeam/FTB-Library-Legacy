package com.feed_the_beast.ftbl.api.client.gui;

import com.feed_the_beast.ftbl.api.EnumSelf;
import com.feed_the_beast.ftbl.api.ForgePlayer;
import com.feed_the_beast.ftbl.api.config.ConfigEntry;
import com.feed_the_beast.ftbl.api.config.ConfigEntryBool;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 15.01.2016.
 */
public class PlayerActionRegistry
{
    public static final ConfigGroup configGroup = new ConfigGroup("sidebar_buttons");
    private static final Map<String, PlayerAction> map = new HashMap<>();

    public static boolean enabled(String id)
    {
        ConfigEntry entry = configGroup.entryMap.get(id);
        return (entry == null) || entry.getAsBoolean();
    }

    public static PlayerAction add(final PlayerAction a)
    {
        if(a != null)
        {
            map.put(a.getID(), a);

            if(a.configDefault() != null)
            {
                ConfigEntryBool entry = new ConfigEntryBool(a.getID(), a.configDefault())
                {
                    @Override
                    public String getFullID()
                    {
                        return "player_action." + a.getID();
                    }
                };

                configGroup.entryMap.put(a.getID(), entry);
            }
        }

        return a;
    }

    public static List<PlayerAction> getPlayerActions(EnumSelf t, ForgePlayer self, ForgePlayer other, boolean sort, boolean ignoreConfig)
    {
        List<PlayerAction> l = new ArrayList<>();

        for(PlayerAction a : map.values())
        {
            if(a.type.equalsType(t) && a.isVisibleFor(self, other))
            {
                if(!ignoreConfig && a.configDefault() != null)
                {
                    if(!enabled(a.getID()))
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

    public static PlayerAction get(String key)
    {
        return map.get(key);
    }
}
