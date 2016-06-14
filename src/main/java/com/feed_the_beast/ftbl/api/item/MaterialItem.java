package com.feed_the_beast.ftbl.api.item;

import latmod.lib.FinalIDObject;
import net.minecraft.item.ItemStack;

public final class MaterialItem extends FinalIDObject
{
    public final int damage;
    public ItemMaterialsLM item;

    public MaterialItem(int d, String s)
    {
        super(s);
        damage = d;
    }

    public MaterialItem setItem(ItemMaterialsLM i)
    {
        item = i;
        return this;
    }

    public ItemStack getStack(int s)
    {
        return new ItemStack(item, s, damage);
    }
}