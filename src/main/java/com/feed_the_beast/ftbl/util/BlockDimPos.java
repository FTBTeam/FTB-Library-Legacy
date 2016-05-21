package com.feed_the_beast.ftbl.util;

import latmod.lib.LMUtils;
import latmod.lib.MathHelperLM;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * Created by LatvianModder on 29.01.2016.
 */
public final class BlockDimPos
{
    public final BlockPos pos;
    public final int dim;

    public BlockDimPos(BlockPos p, int d, boolean copy)
    {
        pos = copy ? new BlockPos(p.getX(), p.getY(), p.getZ()) : p;
        dim = d;
    }

    public BlockDimPos(BlockPos p, int d)
    {
        this(p, d, true);
    }

    public BlockDimPos(int[] ai)
    {
        if(ai == null || ai.length < 4)
        {
            pos = new BlockPos(0, 0, 0);
            dim = 0;
        }
        else
        {
            pos = new BlockPos(ai[0], ai[1], ai[2]);
            dim = ai[3];
        }
    }

    public int[] toIntArray()
    {
        return new int[] {pos.getX(), pos.getY(), pos.getZ(), dim};
    }

    @Override
    public String toString()
    {
        return "[" + dim + '@' + pos.getX() + ',' + pos.getY() + ',' + pos.getZ() + ']';
    }

    @Override
    public boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }
        else
        {
            return o == this || equalsPos((BlockDimPos) o);
        }
    }

    @Override
    public int hashCode()
    {
        return LMUtils.hashCode(pos, dim);
    }

    public Vec3d toVec()
    {
        return new Vec3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
    }

    public EntityDimPos toEntityPos()
    {
        return new EntityDimPos(toVec(), dim);
    }

    public BlockDimPos copy()
    {
        return new BlockDimPos(pos, dim);
    }

    public int chunkX()
    {
        return MathHelperLM.chunk(pos.getX());
    }

    public int chunkY()
    {
        return MathHelperLM.chunk(pos.getY());
    }

    public int chunkZ()
    {
        return MathHelperLM.chunk(pos.getZ());
    }

    public boolean equalsPos(BlockDimPos p)
    {
        if(p == null)
        {
            return false;
        }
        else
        {
            return p == this || (p.dim == dim && p.pos.equals(pos));
        }
    }

    public BlockDimPos offset(EnumFacing facing)
    {
        return new BlockDimPos(pos.offset(facing), dim, false);
    }
}