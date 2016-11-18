package com.feed_the_beast.ftbl.lib.internal;

import com.feed_the_beast.ftbl.lib.Notification;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by LatvianModder on 31.07.2016.
 */
public class FTBLibNotifications
{
    private static Notification create(String s, int v)
    {
        return new Notification(FTBLibFinals.get(s), (byte) v);
    }

    public static final Notification RELOAD_CLIENT_CONFIG = create("reload_client_config", 0).addText(FTBLibLang.RELOAD_CLIENT_CONFIG_1.textComponent()).addText(new TextComponentString("/ftb reload_client")).addText(FTBLibLang.RELOAD_CLIENT_CONFIG_2.textComponent()).setTimer(7000)/*.setColor(0xFF333333)*/;
}