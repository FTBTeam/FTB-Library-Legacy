package com.feed_the_beast.ftbl.lib.math;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;

public final class EntityDimPos
{
	public final Vec3d pos;
	public final int dim;

	public EntityDimPos(Vec3d p, int d)
	{
		pos = new Vec3d(p.x, p.y, p.z);
		dim = d;
	}

	public EntityDimPos(Entity e)
	{
		this(e.getPositionVector(), e.dimension);
	}

	public int hashCode()
	{
		return Arrays.hashCode(new double[] {pos.x, pos.y, pos.z, dim});
	}

	public String toString()
	{
		return "[" + dim + '@' + pos.x + ',' + pos.y + ',' + pos.z + ']';
	}

	public boolean equalsPos(Entity e)
	{
		return pos.x == e.posX && pos.y == e.posY && pos.z == e.posZ && dim == e.dimension;
	}

	public boolean equalsPos(EntityDimPos p)
	{
		return p == this || (pos.x == p.pos.x && pos.y == p.pos.y && pos.z == p.pos.z && dim == p.dim);
	}

	public boolean equals(Object o)
	{
		if (o == null)
		{
			return false;
		}
		else if (o == this)
		{
			return true;
		}
		else if (o instanceof Entity)
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
		return new BlockPos(MathHelper.floor(pos.x), MathHelper.floor(pos.y), MathHelper.floor(pos.z));
	}

	public BlockDimPos toBlockDimPos()
	{
		return new BlockDimPos(toBlockPos(), dim);
	}
}