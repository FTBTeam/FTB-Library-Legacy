package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.INotification;
import com.latmod.lib.util.LMNetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Notification implements INotification
{
    private static final byte FLAG_HAS_TEXT = 1;
    private static final byte FLAG_HAS_ITEM = 2;
    private static final byte FLAG_IS_PERMANENT = 4;

    private ResourceLocation ID;
    private byte variant, color;
    private short timer;
    private boolean isPermanent;
    private List<ITextComponent> text;
    private ItemStack item;

    public Notification(ResourceLocation id, byte v)
    {
        ID = id;
        variant = v;
    }

    public Notification setError(ITextComponent title)
    {
        ITextComponent t = title.createCopy();
        t.getStyle().setColor(TextFormatting.WHITE);
        addText(t);
        timer = 3000;
        color = 97;
        item = new ItemStack(Blocks.BARRIER);
        return this;
    }

    public void setDefaults()
    {
        if(text != null)
        {
            text.clear();
        }

        timer = 3000;
        color = 0;
        item = null;
    }

    public int hashCode()
    {
        return ID.hashCode();
    }

    public boolean equals(Object o)
    {
        return o == this || (o instanceof INotification && ((INotification) o).getID().equals(getID()));
    }

    public String toString()
    {
        return getID().toString();
    }

    public Notification addText(ITextComponent t)
    {
        if(text == null)
        {
            text = new ArrayList<>();
        }

        text.add(t);
        return this;
    }

    @Override
    public ResourceLocation getID()
    {
        return ID;
    }

    @Override
    public byte getVariant()
    {
        return variant;
    }

    @Override
    public List<ITextComponent> getText()
    {
        return text == null ? Collections.emptyList() : text;
    }

    @Override
    public ItemStack getItem()
    {
        return item;
    }

    public Notification setItem(ItemStack is)
    {
        item = is;
        return this;
    }

    @Override
    public boolean isPermanent()
    {
        return isPermanent;
    }

    public void setPermanent(boolean v)
    {
        isPermanent = v;
    }

    @Override
    public short getTimer()
    {
        return timer;
    }

    public Notification setTimer(int t)
    {
        timer = (short) t;
        return this;
    }

    @Override
    public byte getColorID()
    {
        return color;
    }

    public Notification setColorID(byte c)
    {
        color = c;
        return this;
    }

    public static Notification copy(INotification n)
    {
        Notification n1 = new Notification(n.getID(), n.getVariant());
        return n1;
    }

    public static void write(ByteBuf io, INotification n)
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

    public static Notification read(ByteBuf io)
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