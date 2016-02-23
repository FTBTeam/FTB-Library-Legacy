package ftb.lib;

import latmod.lib.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

public final class EntityPos
{
	public final double x, y, z;
	public final int dim;
	
	public EntityPos(double px, double py, double pz, int d)
	{
		x = px;
		y = py;
		z = pz;
		dim = d;
	}
	
	public EntityPos(Entity e)
	{ this(e.posX, e.posY, e.posZ, e.dimension); }
	
	public int hashCode()
	{ return LMUtils.hashCode(x, y, z, dim); }
	
	public String toString()
	{ return "[" + x + ',' + y + ',' + z + ',' + LMDimUtils.getDimName(dim) + ']'; }
	
	public boolean equalsPos(Entity e)
	{ return x == e.posX && y == e.posY && z == e.posZ && dim == e.dimension; }
	
	public boolean equalsPos(EntityPos p)
	{ return (p == this) || (p != null && toBlockDimPos().equalsPos(p.toBlockDimPos())); }
	
	public boolean equals(Object o)
	{
		if(o == null) return false;
		else if(o == this) return true;
		else if(o instanceof Entity) return equalsPos((Entity) o);
		return equalsPos((EntityPos) o);
	}
	
	public Vec3 toVec3()
	{ return new Vec3(x, y, z); }
	
	public EntityPos copy()
	{ return new EntityPos(x, y, z, dim); }
	
	public BlockDimPos toBlockDimPos()
	{ return new BlockDimPos(MathHelperLM.floor(x), MathHelperLM.floor(y), MathHelperLM.floor(z), dim); }
}