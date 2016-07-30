package com.feed_the_beast.ftbl.util;

import com.latmod.lib.math.MathHelperLM;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

/**
 * Created by LatvianModder on 29.01.2016.
 */
public final class BlockDimPos
{
    public final int posX, posY, posZ, dim;

    public BlockDimPos(int x, int y, int z, int d)
    {
        posX = x;
        posY = y;
        posZ = z;
        dim = d;
    }

    public BlockDimPos(Vec3i p, int d)
    {
        this(p.getX(), p.getY(), p.getZ(), d);
    }

    public BlockDimPos(int[] ai)
    {
        this(ai[0], ai[1], ai[2], ai[3]);
    }

    public int[] toIntArray()
    {
        return new int[] {posX, posY, posZ, dim};
    }

    @Override
    public String toString()
    {
        return "[" + dim + '@' + posX + ',' + posY + ',' + posZ + ']';
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
        else if(o instanceof BlockDimPos)
        {
            return equalsPos((BlockDimPos) o);
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return 31 * (31 * (31 * posX + posY) + posZ) + dim;
    }

    public Vec3d toVec()
    {
        return new Vec3d(posX + 0.5D, posY + 0.5D, posZ + 0.5D);
    }

    public EntityDimPos toEntityPos()
    {
        return new EntityDimPos(toVec(), dim);
    }

    public ChunkDimPos toChunkPos()
    {
        return new ChunkDimPos(MathHelperLM.chunk(posX), MathHelperLM.chunk(posZ), dim);
    }

    public BlockPos getBlockPos()
    {
        return new BlockPos(posX, posY, posZ);
    }

    public BlockDimPos copy()
    {
        return new BlockDimPos(posX, posY, posZ, dim);
    }

    public boolean equalsPos(BlockDimPos p)
    {
        return p != null && (p == this || (p.dim == dim && p.posX == posX && p.posY == posY && p.posZ == posZ));
    }
}