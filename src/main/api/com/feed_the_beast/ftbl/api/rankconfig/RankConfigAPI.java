package com.feed_the_beast.ftbl.api.rankconfig;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 16.02.2016.
 */
public class RankConfigAPI
{
    private static final Map<String, RankConfig> map = new HashMap<>();
    static Handler rankConfigHandler;

    public interface Handler
    {
        JsonElement getRankConfig(GameProfile profile, RankConfig config);
    }

    public static void setHandler(Handler handler)
    {
        if(rankConfigHandler != null)
        {
            FTBLibMod.logger.warn("Replacing " + rankConfigHandler.getClass().getName() + " with " + handler.getClass().getName());
        }

        rankConfigHandler = handler;
    }

    public static <E extends RankConfig> E register(E rankConfig)
    {
        if(map.containsKey(rankConfig.getName()))
        {
            throw new RuntimeException("Duplicate RankConfig ID found: " + rankConfig.getName());
        }

        map.put(rankConfig.getName(), rankConfig);
        return rankConfig;
    }

    public static void registerAll(Class<?> c)
    {
        try
        {
            for(Field f : c.getDeclaredFields())
            {
                if(RankConfig.class.isAssignableFrom(f.getType()))
                {
                    register((RankConfig) f.get(null));
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static Collection<RankConfig> getRankConfigValues()
    {
        return map.values();
    }

    public static RankConfig getRankConfig(String s)
    {
        return map.get(s);
    }
}