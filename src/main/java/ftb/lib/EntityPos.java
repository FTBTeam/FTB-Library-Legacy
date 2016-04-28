package ftb.lib;

import latmod.lib.LMUtils;
import latmod.lib.MathHelperLM;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;

public final class EntityPos
{
	public final double x, y, z;
	public final DimensionType dim;
	
	public EntityPos(double px, double py, double pz, DimensionType d)
	{
		x = px;
		y = py;
		z = pz;
		dim = d;
	}
	
	public EntityPos(Entity e)
	{ this(e.posX, e.posY, e.posZ, DimensionType.getById(e.dimension)); }
	
	@Override
	public int hashCode()
	{ return LMUtils.hashCode(x, y, z, dim); }
	
	@Override
	public String toString()
	{ return "[" + dim.getName() + '@' + x + ',' + y + ',' + z + ']'; }
	
	public boolean equalsPos(Entity e)
	{ return x == e.posX && y == e.posY && z == e.posZ && dim.getId() == e.dimension; }
	
	public boolean equalsPos(EntityPos p)
	{ return (p == this) || (p != null && toBlockDimPos().equalsPos(p.toBlockDimPos())); }
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null) return false;
		else if(o == this) return true;
		else if(o instanceof Entity) return equalsPos((Entity) o);
		return equalsPos((EntityPos) o);
	}
	
	public Vec3d toVec3()
	{ return new Vec3d(x, y, z); }
	
	public EntityPos copy()
	{ return new EntityPos(x, y, z, dim); }
	
	public BlockDimPos toBlockDimPos()
	{ return new BlockDimPos(MathHelperLM.floor(x), MathHelperLM.floor(y), MathHelperLM.floor(z), dim); }
}