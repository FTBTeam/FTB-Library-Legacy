package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.INotification;
import com.latmod.lib.util.LMColorUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notification implements INotification
{
    private int ID, timer, color;
    private List<ITextComponent> text;
    private ItemStack item;

    public Notification(int id)
    {
        ID = id;
    }

    public static Notification error(int id, @Nonnull ITextComponent title)
    {
        title.getStyle().setColor(TextFormatting.WHITE);
        return new Notification(id).addText(title).setTimer(3000).setColor(0xFF5959).setItem(new ItemStack(Blocks.BARRIER));
    }

    public void setDefaults()
    {
        if(text != null)
        {
            text.clear();
        }

        timer = 3000;
        color = 0xA0A0A0;
        item = null;
    }

    public int hashCode()
    {
        return ID;
    }

    public boolean equals(Object o)
    {
        return o == this || (o != null && o.hashCode() == ID);
    }

    public String toString()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getID());
        map.put("timer", getTimer());
        map.put("color", LMColorUtils.getHex(getColor()));
        map.put("text", getText());
        map.put("item", getItem());
        map.put("perm", isPermanent());
        return map.toString();
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
    public int getID()
    {
        return ID;
    }

    @Override
    @Nonnull
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
        return false;
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
        color = 0x00FFFFFF & c;
        return this;
    }
}