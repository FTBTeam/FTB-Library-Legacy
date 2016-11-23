package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.NotificationID;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Notification implements INotification
{
    private NotificationID ID;
    private byte color;
    private short timer;
    private boolean isPermanent;
    private List<ITextComponent> text;
    private ItemStack item;

    public Notification(NotificationID id)
    {
        ID = id;
        text = new ArrayList<>();
    }

    public Notification setError(ITextComponent title)
    {
        ITextComponent t = title.createCopy();
        t.getStyle().setColor(TextFormatting.WHITE);
        addText(t);
        timer = 3000;
        color = (byte) 145;
        item = new ItemStack(Blocks.BARRIER);
        return this;
    }

    public void setDefaults()
    {
        text.clear();
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
        return getID() + ", text:" + getText() + ", col:" + getColorID() + ", timer:" + getTimer() + ", item:" + getItem();
    }

    public Notification addText(ITextComponent t)
    {
        text.add(t);
        return this;
    }

    @Override
    public NotificationID getID()
    {
        return ID;
    }

    @Override
    public List<ITextComponent> getText()
    {
        return text;
    }

    @Override
    public ItemStack getItem()
    {
        return item;
    }

    public Notification setItem(@Nullable ItemStack is)
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
        Notification n1 = new Notification(n.getID());
        n1.getText().addAll(n.getText());
        n1.setColorID(n.getColorID());
        n1.setTimer(n.getTimer());
        n1.setItem(n.getItem());
        n1.setPermanent(n.isPermanent());
        return n1;
    }
}