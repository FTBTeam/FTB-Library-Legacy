package com.feed_the_beast.ftbl.lib;

import mcjty.lib.compat.CompatCreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 06.02.2016.
 */
public class CreativeTabBase extends CompatCreativeTabs
{
    private final List<ItemStack> iconItems;
    private long timer = 1000L;

    public CreativeTabBase(String label)
    {
        super(label);
        iconItems = new ArrayList<>();
    }

    public CreativeTabBase addIcon(ItemStack is)
    {
        iconItems.add(is);
        return this;
    }

    public CreativeTabBase setTimer(long t)
    {
        timer = Math.max(50, t);
        return this;
    }

    @Override
    public ItemStack getIconItemStack()
    {
        if(iconItems.isEmpty())
        {
            iconItems.add(new ItemStack(Items.DIAMOND));
        }
        if(iconItems.size() == 1)
        {
            return iconItems.get(0);
        }
        return iconItems.get((int) ((System.currentTimeMillis() / timer) % iconItems.size()));
    }

    @Override
    public Item getItem()
    {
        return getIconItemStack().getItem();
    }

    @Override
    public int getIconItemDamage()
    {
        return getIconItemStack().getItemDamage();
    }

    @Override
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
