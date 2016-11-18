package com.feed_the_beast.ftbl.api.rankconfig;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.google.common.base.Preconditions;
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
        FTBLibFinals.LOGGER.warn("Replacing " + rankConfigHandler.getClass().getName() + " with " + handler.getClass().getName());
        rankConfigHandler = handler;
    }

    public static IRankConfigHandler getHandler()
    {
        return rankConfigHandler;
    }

    public static IRankConfig register(String id, IConfigValue defPlayer, IConfigValue defOP, String description)
    {
        Preconditions.checkArgument(!REGISTRY.containsKey(id), "Duplicate RankConfig ID found: " + id);

        RankConfig c = new RankConfig(id, defPlayer, defOP);
        c.setInfo(description);
        REGISTRY.put(c.getID(), c);
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