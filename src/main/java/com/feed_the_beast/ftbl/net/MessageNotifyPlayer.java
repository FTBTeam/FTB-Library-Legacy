package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.api_impl.FTBLibRegistries;
import com.feed_the_beast.ftbl.client.ClientNotifications;
import com.feed_the_beast.ftbl.client.EnumNotificationDisplay;
import com.feed_the_beast.ftbl.client.FTBLibClientConfig;
import com.latmod.lib.Notification;
import com.latmod.lib.util.LMNetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.List;

public class MessageNotifyPlayer extends MessageToClient<MessageNotifyPlayer>
{
    private static final byte FLAG_HAS_TEXT = 1;
    private static final byte FLAG_HAS_ITEM = 2;
    private static final byte FLAG_IS_PERMANENT = 4;

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

    static Notification copy(INotification n)
    {
        Notification n1 = new Notification(n.getID(), n.getVariant());
        return n1;
    }

    static void write(ByteBuf io, INotification n)
    {
        LMNetUtils.writeResourceLocation(io, n.getID());
        io.writeByte(n.getVariant());
        io.writeByte(n.getColorID());
        io.writeShort(n.getTimer());
        byte flags = 0;

        List<ITextComponent> text = n.getText();
        if(!text.isEmpty())
        {
            flags |= FLAG_HAS_TEXT;
        }

        ItemStack item = n.getItem();
        if(item != null)
        {
            flags |= FLAG_HAS_ITEM;
        }

        if(n.isPermanent())
        {
            flags |= FLAG_IS_PERMANENT;
        }

        io.writeByte(flags);

        if(!text.isEmpty())
        {
            io.writeByte(text.size());

            for(ITextComponent t : text)
            {
                LMNetUtils.writeTextComponent(io, t);
            }
        }

        if(item != null)
        {
            ByteBufUtils.writeItemStack(io, item);
        }
    }

    static Notification read(ByteBuf io)
    {
        ResourceLocation id = LMNetUtils.readResourceLocation(io);
        byte v = io.readByte();
        Notification n = new Notification(id, v);
        n.setColorID(io.readByte());
        n.setTimer(io.readShort());
        byte flags = io.readByte();

        if((flags & FLAG_HAS_TEXT) != 0)
        {
            int s = io.readUnsignedByte();

            while(--s >= 0)
            {
                n.addText(LMNetUtils.readTextComponent(io));
            }
        }

        if((flags & FLAG_HAS_ITEM) != 0)
        {
            n.setItem(ByteBufUtils.readItemStack(io));
        }

        n.setPermanent((flags & FLAG_IS_PERMANENT) != 0);
        return n;
    }
}