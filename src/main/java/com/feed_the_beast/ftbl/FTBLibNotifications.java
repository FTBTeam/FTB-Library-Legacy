package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api_impl.Notification;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by LatvianModder on 31.07.2016.
 */
public class FTBLibNotifications
{
    private static int register(String s)
    {
        return FTBLibAPI.get().getRegistries().notifications().getOrCreateIDFromKey(new ResourceLocation(FTBLibFinals.MOD_ID, s));
    }

    public static final INotification RELOAD_CLIENT_CONFIG = new Notification(register("reload_client_config"))
            .addText(FTBLibLang.reload_client_config_1.textComponent())
            .addText(new TextComponentString("/ftb reload_client"))
            .addText(FTBLibLang.reload_client_config_2.textComponent())
            .setTimer(7000)
            .setColor(0xFF333333);

    public static void init()
    {
    }
}