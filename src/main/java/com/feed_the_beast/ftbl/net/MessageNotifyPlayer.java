package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.api.notification.ClientNotifications;
import com.feed_the_beast.ftbl.api.notification.MouseAction;
import com.feed_the_beast.ftbl.api.notification.Notification;
import com.feed_the_beast.ftbl.util.EnumNotificationDisplay;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageNotifyPlayer extends MessageToClient<MessageNotifyPlayer>
{
    public JsonElement json;
    public int displayType;

    public MessageNotifyPlayer()
    {
    }

    public MessageNotifyPlayer(Notification n, EnumNotificationDisplay e)
    {
        json = n.getSerializableElement();
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
        json = readJsonElement(io);
        displayType = io.readUnsignedByte();
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        writeJsonElement(io, json);
        io.writeByte(displayType);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onMessage(MessageNotifyPlayer m, Minecraft mc)
    {
        EnumNotificationDisplay e = EnumNotificationDisplay.values()[m.displayType];
        Notification n = Notification.deserialize(m.json);

        if(e == EnumNotificationDisplay.SCREEN)
        {
            ClientNotifications.add(n);
        }
        else if(e != EnumNotificationDisplay.OFF)
        {
            if(n.desc != null)
            {
                if(n.mouse == null)
                {
                    n.mouse = new MouseAction();
                }

                n.mouse.hover.add(n.desc);
            }

            GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();

            if(e == EnumNotificationDisplay.CHAT)
            {
                chat.printChatMessageWithOptionalDeletion(n.title, 1); //TODO: Change this to a magic number?
            }
            else
            {
                chat.printChatMessage(n.title);
            }
        }
    }
}