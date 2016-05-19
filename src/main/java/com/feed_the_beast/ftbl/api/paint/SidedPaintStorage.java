package com.feed_the_beast.ftbl.api.paint;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 15.05.2016.
 */
public class SidedPaintStorage implements IPaintable
{
    private Map<EnumFacing, IBlockState> paintMap = new HashMap<>();

    @Override
    public IBlockState getPaint(EnumFacing facing)
    {
        return paintMap.get(facing);
    }

    @Override
    public void setPaint(EnumFacing facing, IBlockState p)
    {
        if(p == null)
        {
            paintMap.remove(facing);
        }
        else
        {
            paintMap.put(facing, p);
        }
    }
}