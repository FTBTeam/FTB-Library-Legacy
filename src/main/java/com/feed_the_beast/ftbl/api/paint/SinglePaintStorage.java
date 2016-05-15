package com.feed_the_beast.ftbl.api.paint;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

/**
 * Created by LatvianModder on 15.05.2016.
 */
public class SinglePaintStorage implements IPaintable
{
	private IBlockState paint;
	
	@Override
	public IBlockState getPaint(EnumFacing facing)
	{ return paint; }
	
	@Override
	public void setPaint(EnumFacing facing, IBlockState p)
	{ paint = p; }
}