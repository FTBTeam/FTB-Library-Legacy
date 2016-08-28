package com.feed_the_beast.ftbl.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 06.02.2016.
 */
public class CreativeTabLM extends CreativeTabs
{
    private final List<ItemStack> iconItems;
    private long timer = 1000L;

    public CreativeTabLM(String label)
    {
        super(label);
        iconItems = new ArrayList<>();
    }

    public CreativeTabLM addIcon(ItemStack is)
    {
        iconItems.add(is);
        return this;
    }

    public CreativeTabLM setTimer(long t)
    {
        timer = Math.max(50, t);
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack()
    {
        if(!iconItems.isEmpty())
        {
            if(iconItems.size() == 1)
            {
                return iconItems.get(0);
            }
            return iconItems.get((int) ((System.currentTimeMillis() / timer) % iconItems.size()));
        }

        return new ItemStack(Items.DIAMOND);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem()
    {
        return getIconItemStack().getItem();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconItemDamage()
    {
        return getIconItemStack().getItemDamage();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void displayAllRelevantItems(List<ItemStack> list)
    {
        for(Item item : Item.REGISTRY)
        {
            if(item != null)
            {
                if(item.getCreativeTab() == this)
                {
                    item.getSubItems(item, this, list);
                }
            }
        }
    }
}
