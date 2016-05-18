package com.feed_the_beast.ftbl.api.paint;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IPainterItem
{
    IBlockState getPaint();
    void setPaint(IBlockState paint);
    boolean canPaintBlocks(ItemStack is);
    void damagePainter(ItemStack is, EntityPlayer ep);
}