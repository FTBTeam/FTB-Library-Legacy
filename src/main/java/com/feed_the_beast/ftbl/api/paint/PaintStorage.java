package com.feed_the_beast.ftbl.api.paint;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by LatvianModder on 15.05.2016.
 */
public class PaintStorage implements IPaintable
{
    private IBlockState paint;

    @Override
    public IBlockState getPaint()
    {
        return paint;
    }

    @Override
    public void setPaint(IBlockState p)
    {
        paint = p;
    }

    @Override
    public boolean canSetPaint(EntityPlayer player, IBlockState paint)
    {
        return getPaint() != paint;
    }

    public void setFromID(int id)
    {
        setPaint(id == 0 ? null : Block.getStateById(id));
    }

    public int getPaintID()
    {
        IBlockState p = getPaint();
        return p == null ? 0 : Block.getStateId(p);
    }
}