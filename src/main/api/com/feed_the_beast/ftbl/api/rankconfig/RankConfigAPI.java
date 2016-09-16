package com.feed_the_beast.ftbl.api.rankconfig;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 16.02.2016.
 */
public class RankConfigAPI
{
    private static final Map<String, IRankConfig> REGISTRY = new HashMap<>();
    private static final Map<String, IRankConfig> REGISTRY_PUBLIC = Collections.unmodifiableMap(REGISTRY);
    private static IRankConfigHandler rankConfigHandler = DefaultRankConfigHandler.INSTANCE;

    public static void setHandler(IRankConfigHandler handler)
    {
        FTBLibMod.logger.warn("Replacing " + rankConfigHandler.getClass().getName() + " with " + handler.getClass().getName());
        rankConfigHandler = handler;
    }

    public static IRankConfigHandler getHandler()
    {
        return rankConfigHandler;
    }

    public static IRankConfig register(String id, IConfigValue defPlayer, IConfigValue defOP, String... description)
    {
        if(REGISTRY.containsKey(id))
        {
            throw new RuntimeException("Duplicate RankConfig ID found: " + id);
        }

        IRankConfig c = new RankConfig(id, defPlayer, defOP, description.length == 0 ? null : description);
        REGISTRY.put(c.getName(), c);
        return c;
    }

    public static IConfigValue getRankConfig(GameProfile profile, IRankConfig id)
    {
        return rankConfigHandler.getRankConfig(profile, id);
    }

    public static IConfigValue getRankConfig(EntityPlayer player, IRankConfig id)
    {
        return getRankConfig(player.getGameProfile(), id);
    }

    public static Map<String, IRankConfig> getRegistredRankConfigs()
    {
        return REGISTRY_PUBLIC;
    }
}