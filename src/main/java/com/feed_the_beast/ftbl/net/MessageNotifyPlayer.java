package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.api_impl.FTBLibRegistries;
import com.feed_the_beast.ftbl.client.ClientNotifications;
import com.feed_the_beast.ftbl.client.EnumNotificationDisplay;
import com.feed_the_beast.ftbl.client.FTBLibClientConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.List;

public class MessageNotifyPlayer extends MessageToClient<MessageNotifyPlayer>
{
    private short ID;

    public MessageNotifyPlayer()
    {
    }

    public MessageNotifyPlayer(short id)
    {
        ID = id;
    }

    @Override
    public LMNetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        ID = io.readShort();
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        io.writeShort(ID);
    }

    @Override
    public void onMessage(MessageNotifyPlayer m)
    {
        EnumNotificationDisplay display = FTBLibClientConfig.NOTIFICATIONS.get();

        if(display == EnumNotificationDisplay.OFF)
        {
            return;
        }

        INotification n = FTBLibRegistries.INSTANCE.CACHED_NOTIFICATIONS.get(m.ID);

        if(n != null)
        {
            displayNotification(display, n, m.ID);
        }
    }

    static void displayNotification(EnumNotificationDisplay display, INotification n, short id)
    {
        if(display == EnumNotificationDisplay.SCREEN)
        {
            ClientNotifications.add(n);
            return;
        }

        List<ITextComponent> list = n.getText();

        if(!list.isEmpty())
        {
            if(list.size() > 1)
            {
                list.get(0).getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, list.get(1)));
            }

            GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();

            if(display == EnumNotificationDisplay.CHAT)
            {
                if(id == 0)
                {
                    id = (short) (n.getID().hashCode() % 32000);
                }

                chat.printChatMessageWithOptionalDeletion(list.get(0), 42059283 + id);
            }
            else
            {
                chat.printChatMessage(list.get(0));
            }
        }
    }
}