package com.feed_the_beast.ftbl.api.paint;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;

public interface IPaintable
{
    IBlockState getPaint();

    void setPaint(IBlockState paint);

    boolean canSetPaint(EntityPlayer player, IBlockState paint);
}