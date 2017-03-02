package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.NotificationId;
import com.feed_the_beast.ftbl.lib.util.LMColorUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Notification implements INotification
{
    private NotificationId id;
    private int color;
    private int timer;
    private List<ITextComponent> text;
    private ItemStack item;

    public Notification(NotificationId i)
    {
        id = i;
        text = new ArrayList<>();
        setDefaults();
    }

    public Notification setError(ITextComponent title)
    {
        ITextComponent t = title.createCopy();
        t.getStyle().setColor(TextFormatting.WHITE);
        addText(t);
        timer = 3000;
        color = 0xFFCC4949;
        item = new ItemStack(Blocks.BARRIER);
        return this;
    }

    public void setDefaults()
    {
        text.clear();
        timer = 3000;
        color = 0xFF606060;
        item = null;
    }

    public int hashCode()
    {
        return id.hashCode();
    }

    public boolean equals(Object o)
    {
        return o == this || (o instanceof INotification && ((INotification) o).getId().equals(getId()));
    }

    public String toString()
    {
        return getId() + ", text:" + getText() + ", col:" + LMColorUtils.getHex(color) + ", timer:" + getTimer() + ", item:" + getItem();
    }

    public Notification addText(ITextComponent t)
    {
        text.add(t);
        return this;
    }

    @Override
    public NotificationId getId()
    {
        return id;
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
    public int getTimer()
    {
        return timer;
    }

    public Notification setTimer(int t)
    {
        timer = t;
        return this;
    }

    @Override
    public int getColor()
    {
        return color;
    }

    public Notification setColor(int c)
    {
        color = c;
        return this;
    }

    public static Notification copy(INotification n)
    {
        Notification n1 = new Notification(n.getId());
        n1.getText().addAll(n.getText());
        n1.setColor(n.getColor());
        n1.setTimer(n.getTimer());
        n1.setItem(n.getItem());
        return n1;
    }
}