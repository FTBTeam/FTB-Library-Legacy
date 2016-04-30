package ftb.lib;

import latmod.lib.LMUtils;
import latmod.lib.MathHelperLM;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.DimensionType;

/**
 * Created by LatvianModder on 29.01.2016.
 */
public final class BlockDimPos
{
	public final BlockPos pos;
	public final DimensionType dim;
	
	public BlockDimPos(Vec3i p, DimensionType d)
	{
		pos = new BlockPos(p.getX(), p.getY(), p.getZ());
		dim = d;
	}
	
	public BlockDimPos(int[] ai)
	{
		if(ai == null || ai.length < 4)
		{
			pos = new BlockPos(0, 0, 0);
			dim = DimensionType.OVERWORLD;
		}
		else
		{
			pos = new BlockPos(ai[0], ai[1], ai[2]);
			dim = DimensionType.getById(ai[3]);
		}
	}
	
	public int[] toIntArray()
	{ return new int[] {pos.getX(), pos.getY(), pos.getZ(), dim.getId()}; }
	
	@Override
	public String toString()
	{ return "[" + dim.getName() + '@' + pos.getX() + ',' + pos.getY() + ',' + pos.getZ() + ']'; }
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null) return false;
		else return o == this || equalsPos((BlockDimPos) o);
	}
	
	@Override
	public int hashCode()
	{ return LMUtils.hashCode(pos, dim); }
	
	public Vec3d toVec()
	{ return new Vec3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D); }
	
	public EntityDimPos toEntityPos()
	{ return new EntityDimPos(toVec(), dim); }
	
	public BlockDimPos copy()
	{ return new BlockDimPos(pos, dim); }
	
	public int chunkX()
	{ return MathHelperLM.chunk(pos.getX()); }
	
	public int chunkY()
	{ return MathHelperLM.chunk(pos.getY()); }
	
	public int chunkZ()
	{ return MathHelperLM.chunk(pos.getZ()); }
	
	public boolean equalsPos(BlockDimPos p)
	{
		if(p == null) return false;
		else return p == this || (p.dim == dim && p.pos.equals(pos));
	}
}