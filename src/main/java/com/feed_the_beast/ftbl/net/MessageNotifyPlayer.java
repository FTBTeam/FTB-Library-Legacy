package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.api.notification.ClientNotifications;
import com.feed_the_beast.ftbl.api.notification.Notification;
import com.feed_the_beast.ftbl.util.EnumNotificationDisplay;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageNotifyPlayer extends MessageToClient<MessageNotifyPlayer>
{
    public Notification notification;
    public int displayType;

    public MessageNotifyPlayer()
    {
    }

    public MessageNotifyPlayer(Notification n, EnumNotificationDisplay e)
    {
        notification = n;
        displayType = e.ordinal();
    }

    @Override
    public LMNetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        notification = new Notification(io.readInt());
        notification.readFromNet(io);
        displayType = io.readUnsignedByte();
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        io.writeInt(notification.ID);
        notification.writeToNet(io);
        io.writeByte(displayType);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onMessage(MessageNotifyPlayer m, Minecraft mc)
    {
        EnumNotificationDisplay e = EnumNotificationDisplay.values()[m.displayType];

        if(e == EnumNotificationDisplay.SCREEN)
        {
            ClientNotifications.add(m.notification);
        }
        else if(e != EnumNotificationDisplay.OFF && !m.notification.text.isEmpty())
        {
            if(m.notification.text.size() > 1)
            {
                m.notification.text.get(0).getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, m.notification.text.get(1)));
            }

            GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();

            if(e == EnumNotificationDisplay.CHAT)
            {
                chat.printChatMessageWithOptionalDeletion(m.notification.text.get(0), m.notification.ID);
            }
            else
            {
                chat.printChatMessage(m.notification.text.get(0));
            }
        }
    }
}