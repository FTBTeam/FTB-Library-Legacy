package ftb.lib;

import net.minecraft.util.EnumFacing;

public enum SidedDirection
{
	BOTTOM(2, 3, 0, 0, 0, 0),
	TOP(3, 2, 1, 1, 1, 1),
	BACK(1, 0, 3, 2, 5, 4),
	FRONT(0, 1, 2, 3, 4, 5),
	LEFT(4, 5, 5, 4, 2, 3),
	RIGHT(5, 4, 4, 5, 3, 2),
	NONE(6, 6, 6, 6, 6, 6);
	
	public final EnumFacing[] directions;
	public final int ID;
	
	SidedDirection(int... i)
	{
		directions = new EnumFacing[6];
		for(int j = 0; j < 6; j++)
			directions[j] = EnumFacing.values()[i[j]];
		ID = ordinal();
	}
	
	// Static //
	
	public static final SidedDirection[] VALUES = new SidedDirection[] {BOTTOM, TOP, BACK, FRONT, LEFT, RIGHT};
	
	public static SidedDirection getSide(EnumFacing side, EnumFacing rot)
	{
		if(side == null || rot == null) return NONE;
		for(SidedDirection VALUE : VALUES) if(VALUE.directions[rot.ordinal()] == side) return VALUE;
		return NONE;
	}
	
	public static SidedDirection get(EnumFacing side, EnumFacing rot3D, EnumFacing rot2D)
	{
		if(side == rot3D) return FRONT;
		if(side == rot3D.getOpposite()) return BACK;
		
		if(rot3D == EnumFacing.DOWN)
		{
			if(side != EnumFacing.DOWN && side != EnumFacing.UP)
			{
				if(rot2D == side) return TOP;
				else if(rot2D == side.getOpposite()) return BOTTOM;
			}
			
			return getSide(side, rot2D);
		}
		else if(rot3D == EnumFacing.UP)
		{
			if(side != EnumFacing.DOWN && side != EnumFacing.UP)
			{
				if(rot2D == side) return BOTTOM;
				else if(rot2D == side.getOpposite()) return TOP;
			}
			
			return getSide(side, rot2D);
		}
		else
		{
			if(side == EnumFacing.DOWN) return BOTTOM;
			else if(side == EnumFacing.UP) return TOP;
			return getSide(side, rot3D);
		}
	}
}