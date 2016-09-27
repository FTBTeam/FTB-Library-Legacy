package com.feed_the_beast.ftbl.lib.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ItemBlockLM extends ItemBlock
{
    public ItemBlockLM(Block b)
    {
        super(b);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public int getMetadata(int m)
    {
        return m;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        String s = getVariantName(stack.getMetadata());
        return (s == null) ? getBlock().getUnlocalizedName() : (getRegistryName().getResourceDomain() + ".tile." + s);
    }

    @Nullable
    public String getVariantName(int meta)
    {
        return null;
    }
}