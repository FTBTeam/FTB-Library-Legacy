package ftb.lib;

import latmod.lib.LMUtils;
import latmod.lib.MathHelperLM;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;

/**
 * Created by LatvianModder on 29.01.2016.
 */
public final class BlockDimPos
{
	public final int x, y, z;
	public final DimensionType dim;
	
	public BlockDimPos(int px, int py, int pz, DimensionType d)
	{
		x = px;
		y = py;
		z = pz;
		dim = d;
	}
	
	public BlockDimPos(int[] ai)
	{
		if(ai == null || ai.length < 4)
		{
			x = 0;
			y = 256;
			z = 0;
			dim = DimensionType.OVERWORLD;
		}
		else
		{
			x = ai[0];
			y = ai[1];
			z = ai[2];
			dim = DimensionType.getById(ai[3]);
		}
	}
	
	public BlockDimPos(BlockPos pos, DimensionType dim)
	{ this(pos.getX(), pos.getY(), pos.getZ(), dim); }
	
	public boolean isValid()
	{ return y >= 0 && y < 256; }
	
	public int[] toIntArray()
	{ return new int[] {x, y, z, dim.getId()}; }
	
	@Override
	public String toString()
	{ return "[" + dim.getName() + '@' + x + ',' + y + ',' + z + ']'; }
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null) return false;
		else return o == this || equalsPos((BlockDimPos) o);
	}
	
	@Override
	public int hashCode()
	{ return LMUtils.hashCode(x, y, z, dim); }
	
	public EntityPos toEntityPos()
	{ return new EntityPos(x + 0.5D, y + 0.5D, z + 0.5D, dim); }
	
	public BlockDimPos copy()
	{ return new BlockDimPos(x, y, z, dim); }
	
	public BlockPos toBlockPos()
	{ return new BlockPos(x, y, z); }
	
	public int chunkX()
	{ return MathHelperLM.chunk(x); }
	
	public int chunkY()
	{ return MathHelperLM.chunk(y); }
	
	public int chunkZ()
	{ return MathHelperLM.chunk(z); }
	
	public boolean equalsPos(BlockDimPos p)
	{
		if(p == null) return false;
		else return p == this || (p.dim == dim && p.x == x && p.y == y && p.z == z);
	}
}