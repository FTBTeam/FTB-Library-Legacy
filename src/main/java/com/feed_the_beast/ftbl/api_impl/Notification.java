package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.INotification;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Notification implements INotification
{
    private ResourceLocation ID;
    private int timer, color;
    private List<ITextComponent> text;
    private ItemStack item;

    public Notification(ResourceLocation id)
    {
        ID = id;
    }

    public static Notification error(ResourceLocation id, ITextComponent title)
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