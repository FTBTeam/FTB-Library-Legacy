package com.feed_the_beast.ftbl.lib.item;

import net.minecraft.item.Item;

public abstract class ItemLM extends Item
{
    public ItemLM(String id)
    {
        setRegistryName(id);
        setUnlocalizedName(id.replace(':', '.'));
    }
}