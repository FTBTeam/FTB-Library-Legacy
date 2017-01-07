package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.client.EnumNotificationDisplay;
import com.feed_the_beast.ftbl.client.FTBLibClientConfig;
import com.feed_the_beast.ftbl.lib.Notification;
import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class MessageNotifyPlayerCustom extends MessageToClient<MessageNotifyPlayerCustom>
{
    private INotification notification;

    public MessageNotifyPlayerCustom()
    {
    }

    public MessageNotifyPlayerCustom(INotification n)
    {
        notification = Notification.copy(n);
    }

    @Override
    public LMNetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        notification = MessageNotifyPlayer.read(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        MessageNotifyPlayer.write(io, notification);
    }

    @Override
    public void onMessage(MessageNotifyPlayerCustom m, EntityPlayer player)
    {
        EnumNotificationDisplay display = FTBLibClientConfig.NOTIFICATIONS.getNonnull();

        if(display != EnumNotificationDisplay.OFF)
        {
            MessageNotifyPlayer.displayNotification(display, m.notification);
        }
    }
}