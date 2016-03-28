package ftb.lib;

import ftb.lib.mod.FTBLibMod;
import latmod.lib.MathHelperLM;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.*;

public class MathHelperMC
{
	public static EnumFacing getHorizontalFacing(int index)
	{
		if(index == 0) return EnumFacing.NORTH;
		else if(index == 1) return EnumFacing.EAST;
		else if(index == 2) return EnumFacing.SOUTH;
		else if(index == 3) return EnumFacing.WEST;
		return null;
	}
	
	public static int getHorizontalIndex(EnumFacing facing)
	{
		if(facing == EnumFacing.NORTH) return 0;
		else if(facing == EnumFacing.EAST) return 1;
		else if(facing == EnumFacing.SOUTH) return 2;
		else if(facing == EnumFacing.WEST) return 3;
		return -1;
	}
	
	public static EnumFacing get2DRotation(EntityLivingBase el)
	{ return getHorizontalFacing(MathHelperLM.getRotations(el.rotationYaw, 4)); }
	
	public static EnumFacing get3DRotation(World w, BlockPos pos, EntityLivingBase el)
	{ return BlockPistonBase.getFacingFromEntity(w, pos, el); }
	
	public static EnumFacing getDirection(Vec3i p0, Vec3i p1)
	{
		if(p0 != null && p1 != null)
		{
			int x0 = p0.getX();
			int y0 = p0.getY();
			int z0 = p0.getZ();
			int x1 = p1.getX();
			int y1 = p1.getY();
			int z1 = p1.getZ();
			
			if(x0 == x1 || y0 == y1 || z0 == z1)
			{
				if(x0 == x1 && y0 == y1 && z0 == z1) return null;
				else if(x0 != x1) return x0 > x1 ? EnumFacing.EAST : EnumFacing.WEST;
				else if(y0 != y1) return y0 > y1 ? EnumFacing.UP : EnumFacing.WEST;
				else if(z0 != z1) return z0 > z1 ? EnumFacing.SOUTH : EnumFacing.NORTH;
			}
		}
		
		return null;
	}
	
	public static double getDistance(Vec3i p0, Vec3i p1)
	{
		if(p0 == null || p1 == null) return 0D;
		return MathHelperLM.dist(p0.getX(), p0.getY(), p0.getZ(), p1.getX(), p1.getY(), p1.getZ());
	}
	
	public static Vec3 randomAABB(Random r, AxisAlignedBB bb)
	{
		double x = MathHelperLM.randomDouble(r, bb.minX, bb.maxX);
		double y = MathHelperLM.randomDouble(r, bb.minY, bb.maxY);
		double z = MathHelperLM.randomDouble(r, bb.minZ, bb.maxZ);
		return new Vec3(x, y, z);
	}
	
	public static Vec3 getEyePosition(EntityPlayer ep)
	{
		double y = 0D;
		if(!ep.worldObj.isRemote) y = ep.getEyeHeight();
		return new Vec3(ep.posX, ep.posY + y, ep.posZ);
	}
	
	public static MovingObjectPosition rayTrace(EntityPlayer ep, double d)
	{
		if(ep == null) return null;
		Vec3 pos = getEyePosition(ep);
		Vec3 look = ep.getLookVec();
		Vec3 vec = pos.addVector(look.xCoord * d, look.yCoord * d, look.zCoord * d);
		MovingObjectPosition mop = ep.worldObj.rayTraceBlocks(pos, vec, false, true, false);
		if(mop != null && mop.hitVec == null) mop.hitVec = new Vec3(0D, 0D, 0D);
		return mop;
	}
	
	public static MovingObjectPosition rayTrace(EntityPlayer ep)
	{ return rayTrace(ep, FTBLibMod.proxy.getReachDist(ep)); }
	
	public static MovingObjectPosition collisionRayTrace(World w, BlockPos blockPos, Vec3 start, Vec3 end, AxisAlignedBB[] boxes)
	{
		if(boxes == null || boxes.length <= 0) return null;
		
		MovingObjectPosition current = null;
		double dist = Double.POSITIVE_INFINITY;
		
		for(int i = 0; i < boxes.length; i++)
		{
			if(boxes[i] != null)
			{
				MovingObjectPosition mop = collisionRayTrace(w, blockPos, start, end, boxes[i]);
				
				if(mop != null)
				{
					double d1 = mop.hitVec.squareDistanceTo(start);
					if(current == null || d1 < dist)
					{
						current = mop;
						current.subHit = i;
						dist = d1;
					}
				}
			}
		}
		
		return current;
	}
	
	public static MovingObjectPosition collisionRayTrace(World w, BlockPos blockPos, Vec3 start, Vec3 end, List<AxisAlignedBB> boxes)
	{
		AxisAlignedBB[] boxesa = new AxisAlignedBB[boxes.size()];
		for(int i = 0; i < boxesa.length; i++) boxesa[i] = boxes.get(i).addCoord(0D, 0D, 0D);
		return collisionRayTrace(w, blockPos, start, end, boxesa);
	}
	
	public static MovingObjectPosition collisionRayTrace(World w, BlockPos blockPos, Vec3 start, Vec3 end, AxisAlignedBB aabb)
	{
		Vec3 pos = start.addVector(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ());
		Vec3 rot = end.addVector(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ());
		
		Vec3 xmin = pos.getIntermediateWithXValue(rot, aabb.minX);
		Vec3 xmax = pos.getIntermediateWithXValue(rot, aabb.maxX);
		Vec3 ymin = pos.getIntermediateWithYValue(rot, aabb.minY);
		Vec3 ymax = pos.getIntermediateWithYValue(rot, aabb.maxY);
		Vec3 zmin = pos.getIntermediateWithZValue(rot, aabb.minZ);
		Vec3 zmax = pos.getIntermediateWithZValue(rot, aabb.maxZ);
		
		if(!isVecInsideYZBounds(xmin, aabb)) xmin = null;
		if(!isVecInsideYZBounds(xmax, aabb)) xmax = null;
		if(!isVecInsideXZBounds(ymin, aabb)) ymin = null;
		if(!isVecInsideXZBounds(ymax, aabb)) ymax = null;
		if(!isVecInsideXYBounds(zmin, aabb)) zmin = null;
		if(!isVecInsideXYBounds(zmax, aabb)) zmax = null;
		Vec3 v = null;
		
		if(xmin != null && (v == null || pos.squareDistanceTo(xmin) < pos.squareDistanceTo(v))) v = xmin;
		if(xmax != null && (v == null || pos.squareDistanceTo(xmax) < pos.squareDistanceTo(v))) v = xmax;
		if(ymin != null && (v == null || pos.squareDistanceTo(ymin) < pos.squareDistanceTo(v))) v = ymin;
		if(ymax != null && (v == null || pos.squareDistanceTo(ymax) < pos.squareDistanceTo(v))) v = ymax;
		if(zmin != null && (v == null || pos.squareDistanceTo(zmin) < pos.squareDistanceTo(v))) v = zmin;
		if(zmax != null && (v == null || pos.squareDistanceTo(zmax) < pos.squareDistanceTo(v))) v = zmax;
		if(v == null) return null;
		else
		{
			EnumFacing side = null;
			
			if(v == xmin) side = EnumFacing.WEST;
			if(v == xmax) side = EnumFacing.EAST;
			if(v == ymin) side = EnumFacing.DOWN;
			if(v == ymax) side = EnumFacing.UP;
			if(v == zmin) side = EnumFacing.NORTH;
			if(v == zmax) side = EnumFacing.SOUTH;
			
			return new MovingObjectPosition(v.addVector(blockPos.getX(), blockPos.getY(), blockPos.getZ()), side, blockPos);
		}
	}
	
	private static boolean isVecInsideYZBounds(Vec3 v, AxisAlignedBB aabb)
	{ return v != null && (v.yCoord >= aabb.minY && v.yCoord <= aabb.maxY && v.zCoord >= aabb.minZ && v.zCoord <= aabb.maxZ); }
	
	private static boolean isVecInsideXZBounds(Vec3 v, AxisAlignedBB aabb)
	{ return v != null && (v.xCoord >= aabb.minX && v.xCoord <= aabb.maxX && v.zCoord >= aabb.minZ && v.zCoord <= aabb.maxZ); }
	
	private static boolean isVecInsideXYBounds(Vec3 v, AxisAlignedBB aabb)
	{ return v != null && (v.xCoord >= aabb.minX && v.xCoord <= aabb.maxX && v.yCoord >= aabb.minY && v.yCoord <= aabb.maxY); }
	
	public static MovingObjectPosition getMOPFrom(BlockPos pos, EnumFacing s, float hitX, float hitY, float hitZ)
	{ return new MovingObjectPosition(new Vec3(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ), s, pos); }
	
	public static AxisAlignedBB getBox(double cx, double y0, double cz, double w, double y1, double d)
	{ return new AxisAlignedBB(cx - w / 2D, y0, cz - d / 2D, cx + w / 2D, y1, cz + d / 2D); }
	
	public static AxisAlignedBB centerBox(double x, double y, double z, double w, double h, double d)
	{ return getBox(x, y - h / 2D, z, w, y + h / 2D, d); }
	
	public static final int[][] connectedTextureMap = {{0, 0, 0, 0}, {1, 1, 1, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}, {1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}, {1, 1, 0, 0}, {0, 1, 1, 0}, {0, 0, 1, 1}, {1, 0, 0, 1}, {0, 1, 1, 1}, {1, 0, 1, 1}, {1, 1, 0, 1}, {1, 1, 1, 0}};
	
	public static final int getConnectedTextureIndex(int a, int b, int c, int d)
	{
		for(int i = 0; i < 16; i++)
		{
			if(connectedTextureMap[i][0] == a && connectedTextureMap[i][1] == b && connectedTextureMap[i][2] == c && connectedTextureMap[i][3] == d)
			{
				return i;
			}
		}
		
		return -1;
	}
}