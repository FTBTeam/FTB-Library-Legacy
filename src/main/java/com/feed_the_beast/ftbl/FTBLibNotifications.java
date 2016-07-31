package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.notification.NotificationID;
import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 31.07.2016.
 */
public class FTBLibNotifications
{
    public static final int RELOAD_CLIENT_CONFIG = get("reload_client_config");

    private static int get(String id)
    {
        return NotificationID.get(new ResourceLocation(FTBLibFinals.MOD_ID, id));
    }
}