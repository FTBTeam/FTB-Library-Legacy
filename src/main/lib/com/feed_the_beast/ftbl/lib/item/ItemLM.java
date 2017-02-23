package com.feed_the_beast.ftbl.lib.item;

import net.minecraft.item.Item;

public class ItemLM extends Item
{
    public ItemLM(String id)
    {
        setRegistryName(id);
        setUnlocalizedName(id.replace(':', '.'));
    }
}