package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.api.notification.ClientNotifications;
import com.feed_the_beast.ftbl.api.notification.Notification;
import com.feed_the_beast.ftbl.client.FTBLibModClient;
import com.feed_the_beast.ftbl.util.EnumScreen;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageNotifyPlayer extends MessageToClient<MessageNotifyPlayer>
{
    public JsonElement json;

    public MessageNotifyPlayer()
    {
    }

    public MessageNotifyPlayer(Notification n)
    {
        json = n.getSerializableElement();
    }

    @Override
    public LMNetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        json = readJsonElement(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        writeJsonElement(io, json);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onMessage(MessageNotifyPlayer m, Minecraft mc)
    {
        if(FTBLibModClient.notifications.get() != EnumScreen.OFF)
        {
            Notification n = Notification.deserialize(m.json);

            if(FTBLibModClient.notifications.get() == EnumScreen.SCREEN)
            {
                ClientNotifications.add(n);
            }
            else
            {
                mc.thePlayer.addChatMessage(n.title);
                if(n.desc != null)
                {
                    mc.thePlayer.addChatMessage(n.desc);
                }
            }
        }
    }
}