package com.feed_the_beast.ftbl.lib.item;

import mcjty.lib.compat.CompatItem;

public class ItemBase extends CompatItem
{
    public ItemBase(String id)
    {
        setRegistryName(id);
        setUnlocalizedName(id.replace(':', '.'));
    }
}