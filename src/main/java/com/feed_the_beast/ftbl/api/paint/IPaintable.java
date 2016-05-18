package com.feed_the_beast.ftbl.api.paint;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;

public interface IPaintable
{
    IBlockState getPaint(EnumFacing facing);
    void setPaint(EnumFacing facing, IBlockState paint);
    
    default boolean canSetPaint(EntityPlayer player, EnumFacing facing, IBlockState paint)
    { return getPaint(facing) != paint; }
}