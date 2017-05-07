package com.feed_the_beast.ftbl.lib.item;

import net.minecraft.item.Item;

public class ItemBase extends Item
{
    public ItemBase(String id)
    {
        setRegistryName(id);
        setUnlocalizedName(id.replace(':', '.'));
    }
}