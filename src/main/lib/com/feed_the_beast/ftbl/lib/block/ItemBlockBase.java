package com.feed_the_beast.ftbl.lib.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockBase extends ItemBlock
{
    public ItemBlockBase(Block b, boolean hasSubtypes)
    {
        super(b);

        if(hasSubtypes)
        {
            setHasSubtypes(true);
            setMaxDamage(0);
        }
    }

    public ItemBlockBase(Block b)
    {
        this(b, false);
    }

    @Override
    public int getMetadata(int metadata)
    {
        return getHasSubtypes() ? metadata : 0;
    }
}