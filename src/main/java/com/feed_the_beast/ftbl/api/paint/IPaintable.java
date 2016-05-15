package com.feed_the_beast.ftbl.api.paint;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;

public interface IPaintable
{
	IBlockState getPaint(EnumFacing facing);
	void setPaint(EnumFacing facing, IBlockState paint);
	
	default boolean canSetPaint(EntityPlayer player, RayTraceResult hit, IBlockState paint)
	{ return getPaint(hit.sideHit) != paint; }
}