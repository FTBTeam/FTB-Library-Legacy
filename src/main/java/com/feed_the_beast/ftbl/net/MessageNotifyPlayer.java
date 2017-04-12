package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.NotificationId;
import com.feed_the_beast.ftbl.api_impl.SharedClientData;
import com.feed_the_beast.ftbl.client.EnumNotificationDisplay;
import com.feed_the_beast.ftbl.client.FTBLibClientConfig;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.Notification;
import com.feed_the_beast.ftbl.lib.io.Bits;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.List;

public class MessageNotifyPlayer extends MessageToClient<MessageNotifyPlayer>
{
    private static final int FLAG_HAS_TEXT = 1;
    private static final int FLAG_HAS_ITEM = 2;

    private NotificationId ID;

    public MessageNotifyPlayer()
    {
    }

    public MessageNotifyPlayer(NotificationId id)
    {
        ID = id;
    }

    @Override
    public NetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        ID = readID(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        writeID(io, ID);
    }

    @Override
    public void onMessage(MessageNotifyPlayer m, EntityPlayer player)
    {
        EnumNotificationDisplay display = FTBLibClientConfig.NOTIFICATIONS.getNonnull();

        if(display == EnumNotificationDisplay.OFF)
        {
            return;
        }

        INotification n = SharedClientData.INSTANCE.notifications.get(m.ID);

        if(n != null)
        {
            FTBLibMod.PROXY.displayNotification(display, n);
        }
    }

    private static void writeID(ByteBuf io, NotificationId id)
    {
        NetUtils.writeResourceLocation(io, id.getID());
        io.writeByte(id.getVariant());
    }

    private static NotificationId readID(ByteBuf io)
    {
        ResourceLocation id = NetUtils.readResourceLocation(io);
        return new NotificationId(id, io.readByte());
    }

    static void write(ByteBuf io, INotification n)
    {
        writeID(io, n.getId());
        io.writeInt(n.getColor().rgba());
        io.writeShort(n.getTimer());
        int flags = 0;

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

        io.writeByte(flags);

        if(!text.isEmpty())
        {
            io.writeByte(text.size());

            for(ITextComponent t : text)
            {
                NetUtils.writeTextComponent(io, t);
            }
        }

        if(item != null)
        {
            ByteBufUtils.writeItemStack(io, item);
        }
    }

    static Notification read(ByteBuf io)
    {
        Notification n = new Notification(readID(io));
        n.setColor(new Color4I(true, io.readInt()));
        n.setTimer(io.readUnsignedShort());
        int flags = io.readUnsignedByte();

        if(Bits.getFlag(flags, FLAG_HAS_TEXT))
        {
            int s = io.readUnsignedByte();

            while(--s >= 0)
            {
                n.addText(NetUtils.readTextComponent(io));
            }
        }

        if(Bits.getFlag(flags, FLAG_HAS_ITEM))
        {
            n.setItem(ByteBufUtils.readItemStack(io));
        }

        return n;
    }
}