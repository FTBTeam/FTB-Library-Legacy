package com.feed_the_beast.ftbl.util;

import com.latmod.lib.math.MathHelperLM;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

/**
 * Created by LatvianModder on 29.01.2016.
 */
public final class BlockDimPos extends BlockPos
{
    private int dim;

    public BlockDimPos(Vec3i p, int d)
    {
        super(p);
        dim = d;
    }

    public BlockDimPos(int x, int y, int z, int d)
    {
        super(x, y, z);
        dim = d;
    }

    public BlockDimPos(int[] ai)
    {
        this(ai[0], ai[1], ai[2], ai[3]);
    }

    public int getDim()
    {
        return dim;
    }

    public int[] toIntArray()
    {
        return new int[] {getX(), getY(), getZ(), dim};
    }

    @Override
    public String toString()
    {
        return "[" + dim + '@' + getX() + ',' + getY() + ',' + getZ() + ']';
    }

    @Override
    public boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }
        else if(o == this)
        {
            return true;
        }
        else if(o instanceof BlockPos)
        {
            if(o instanceof BlockDimPos)
            {
                return equalsPos((BlockDimPos) o);
            }
            else
            {
                return super.equals(o);
            }
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return super.hashCode() * 31 + getDim();
    }

    public Vec3d toVec()
    {
        return new Vec3d(getX() + 0.5D, getY() + 0.5D, getZ() + 0.5D);
    }

    public EntityDimPos toEntityPos()
    {
        return new EntityDimPos(toVec(), getDim());
    }

    public ChunkDimPos toChunkPos()
    {
        return new ChunkDimPos(getDim(), MathHelperLM.chunk(getX()), MathHelperLM.chunk(getZ()));
    }

    public BlockDimPos copy()
    {
        return new BlockDimPos(getX(), getY(), getZ(), getDim());
    }

    public int chunkX()
    {
        return MathHelperLM.chunk(getX());
    }

    public int chunkY()
    {
        return MathHelperLM.chunk(getY());
    }

    public int chunkZ()
    {
        return MathHelperLM.chunk(getZ());
    }

    public boolean equalsPos(BlockDimPos p)
    {
        return p != null && (p == this || (p.getDim() == getDim() && p.getX() == getX() && p.getY() == getY() && p.getZ() == getZ()));
    }

    @Override
    public BlockDimPos offset(EnumFacing facing, int n)
    {
        return n == 0 ? this : new BlockDimPos(getX() + facing.getFrontOffsetX() * n, getY() + facing.getFrontOffsetY() * n, getZ() + facing.getFrontOffsetZ() * n, getDim());
    }
}