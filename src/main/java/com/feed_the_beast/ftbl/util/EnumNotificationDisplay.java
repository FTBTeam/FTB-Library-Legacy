package com.feed_the_beast.ftbl.util;

import com.feed_the_beast.ftbl.api.config.EnumNameMap;
import com.feed_the_beast.ftbl.api.notification.MouseAction;
import com.feed_the_beast.ftbl.api.notification.Notification;
import com.feed_the_beast.ftbl.net.MessageNotifyPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public enum EnumNotificationDisplay
{
    OFF,
    SCREEN,
    CHAT,
    CHAT_ALL;

    public static final EnumNameMap<EnumNotificationDisplay> NAME_MAP = new EnumNameMap<>(false, values());

    public void displayNotification(EntityPlayerMP player, Notification n)
    {
        if(this == SCREEN)
        {
            new MessageNotifyPlayer(n).sendTo(player);
        }
        else if(this != OFF)
        {
            if(n.desc != null)
            {
                if(n.mouse == null)
                {
                    n.mouse = new MouseAction();
                }

                n.mouse.hover.add(n.desc);
            }

            if(this == CHAT)
            {
                player.addChatComponentMessage(n.title);
            }
            else
            {
                player.addChatMessage(n.title);
            }
        }
    }
}