package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 31.07.2016.
 */
public class FTBLibNotifications
{
    public static final int RELOAD_CLIENT_CONFIG = register("reload_client_config");

    private static int register(String s)
    {
        return FTBLibAPI.get().getRegistries().notifications().getOrCreateIDFromKey(new ResourceLocation(FTBLibFinals.MOD_ID, s));
    }

    public static void init()
    {
    }
}