package com.latmod.lib.math;

import com.latmod.lib.util.LMUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class EntityDimPos
{
    public final Vec3d pos;
    public final int dim;

    public EntityDimPos(Vec3d p, int d)
    {
        pos = new Vec3d(p.xCoord, p.yCoord, p.zCoord);
        dim = d;
    }

    public EntityDimPos(Entity e)
    {
        this(e.getPositionVector(), e.dimension);
    }

    public int hashCode()
    {
        return LMUtils.hashCode(pos, dim);
    }

    public String toString()
    {
        return "[" + dim + '@' + pos.xCoord + ',' + pos.yCoord + ',' + pos.zCoord + ']';
    }

    public boolean equalsPos(Entity e)
    {
        return pos.xCoord == e.posX && pos.yCoord == e.posY && pos.zCoord == e.posZ && dim == e.dimension;
    }

    public boolean equalsPos(EntityDimPos p)
    {
        return (p == this) || (p != null && pos.xCoord == p.pos.xCoord && pos.yCoord == p.pos.yCoord && pos.zCoord == p.pos.zCoord && dim == p.dim);
    }

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
        else if(o instanceof Entity)
        {
            return equalsPos((Entity) o);
        }
        return equalsPos((EntityDimPos) o);
    }

    public EntityDimPos copy()
    {
        return new EntityDimPos(pos, dim);
    }

    public BlockPos toBlockPos()
    {
        return new BlockPos(MathHelper.floor_double(pos.xCoord), MathHelper.floor_double(pos.yCoord), MathHelper.floor_double(pos.zCoord));
    }

    public BlockDimPos toBlockDimPos()
    {
        return new BlockDimPos(toBlockPos(), dim);
    }
}