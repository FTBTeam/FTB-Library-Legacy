package com.feed_the_beast.ftblib.lib.math;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

/**
 * @author LatvianModder
 */
public final class ChunkDimPos
{
	public final int posX, posZ, dim;

	public ChunkDimPos(int x, int z, int d)
	{
		posX = x;
		posZ = z;
		dim = d;
	}

	public ChunkDimPos(ChunkPos pos, int d)
	{
		this(pos.x, pos.z, d);
	}

	public ChunkDimPos(BlockPos pos, int d)
	{
		this(MathUtils.chunk(pos.getX()), MathUtils.chunk(pos.getZ()), d);
	}

	public ChunkDimPos(Entity entity)
	{
		this(MathUtils.chunk(entity.posX), MathUtils.chunk(entity.posZ), entity.dimension);
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
		else if (o instanceof ChunkDimPos)
		{
			return equalsChunkDimPos((ChunkDimPos) o);
		}
		return false;
	}

	public boolean equalsChunkDimPos(ChunkDimPos p)
	{
		return p == this || (p.dim == dim && p.posX == posX && p.posZ == posZ);
	}

	public String toString()
	{
		return "[" + dim + '@' + posX + ',' + posZ + ']';
	}

	public int hashCode()
	{
		return 31 * (31 * posX + posZ) + dim;
	}

	public ChunkPos getChunkPos()
	{
		return new ChunkPos(posX, posZ);
	}
}