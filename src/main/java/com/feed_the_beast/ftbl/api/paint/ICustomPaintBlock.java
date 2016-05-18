package com.feed_the_beast.ftbl.api.paint;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;

public interface ICustomPaintBlock
{
    IBlockState getCustomPaint(IBlockAccess w, IBlockState state, RayTraceResult hit);
}