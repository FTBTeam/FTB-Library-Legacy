package com.feed_the_beast.ftbl.lib.math;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 19.03.2017.
 */
public class BlockPosContainer
{
    private final World world;
    private final BlockPos pos;
    private IBlockState state;

    public BlockPosContainer(World w, BlockPos p, @Nullable IBlockState s)
    {
        world = w;
        pos = p;
        state = s;
    }

    public BlockPosContainer(PlayerInteractEvent event)
    {
        this(event.getWorld(), event.getPos(), event.getWorld().getBlockState(event.getPos()));
    }

    public World getWorld()
    {
        return world;
    }

    public BlockPos getPos()
    {
        return pos;
    }

    public BlockPosContainer resetState()
    {
        state = null;
        return this;
    }

    public IBlockState getState()
    {
        if(state == null)
        {
            state = getWorld().getBlockState(getPos());
        }

        return state;
    }

    public boolean equals(Object o)
    {
        if(o == this)
        {
            return true;
        }
        else if(o instanceof BlockPosContainer)
        {
            BlockPosContainer c = (BlockPosContainer) o;
            return c.getWorld() == getWorld() && c.getPos().equals(getPos());
        }

        return false;
    }

    public int hashCode()
    {
        return getWorld().hashCode() ^ getPos().hashCode();
    }

    public String toString()
    {
        return getState().toString() + '@' + getPos().getX() + ',' + getPos().getY() + ',' + getPos().getZ() + '@' + getWorld().provider.getDimension();
    }
}