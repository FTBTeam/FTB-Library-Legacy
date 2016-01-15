package ftb.lib;

import ftb.lib.mod.FTBLibMod;
import latmod.lib.MathHelperLM;
import latmod.lib.util.VecLM;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.*;

public class MathHelperMC
{
	public static EnumFacing get2DRotation(EntityLivingBase el)
	{
		//int i = floor(el.rotationYaw * 4D / 360D + 0.5D) & 3;
		int i = MathHelperLM.getRotations(el.rotationYaw, 4);
		if(i == 0) return EnumFacing.NORTH;
		else if(i == 1) return EnumFacing.EAST;
		else if(i == 2) return EnumFacing.SOUTH;
		else if(i == 3) return EnumFacing.WEST;
		return null;
	}
	
	public static EnumFacing get3DRotation(World w, BlockPos pos, EntityLivingBase el)
	{ return BlockPistonBase.getFacingFromEntity(w, pos, el); }
	
	public static VecLM randomAABB(Random r, AxisAlignedBB bb)
	{
		double x = MathHelperLM.randomDouble(r, bb.minX, bb.maxX);
		double y = MathHelperLM.randomDouble(r, bb.minY, bb.maxY);
		double z = MathHelperLM.randomDouble(r, bb.minZ, bb.maxZ);
		return new VecLM(x, y, z);
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
	{ return v == null ? false : v.yCoord >= aabb.minY && v.yCoord <= aabb.maxY && v.zCoord >= aabb.minZ && v.zCoord <= aabb.maxZ; }
	
	private static boolean isVecInsideXZBounds(Vec3 v, AxisAlignedBB aabb)
	{ return v == null ? false : v.xCoord >= aabb.minX && v.xCoord <= aabb.maxX && v.zCoord >= aabb.minZ && v.zCoord <= aabb.maxZ; }
	
	private static boolean isVecInsideXYBounds(Vec3 v, AxisAlignedBB aabb)
	{ return v == null ? false : v.xCoord >= aabb.minX && v.xCoord <= aabb.maxX && v.yCoord >= aabb.minY && v.yCoord <= aabb.maxY; }
	
	public static MovingObjectPosition getMOPFrom(BlockPos pos, EnumFacing s, float hitX, float hitY, float hitZ)
	{ return new MovingObjectPosition(new Vec3(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ), s, pos); }
	
	public static AxisAlignedBB getBox(double cx, double y0, double cz, double w, double y1, double d)
	{ return new AxisAlignedBB(cx - w / 2D, y0, cz - d / 2D, cx + w / 2D, y1, cz + d / 2D); }
	
	public static AxisAlignedBB centerBox(double x, double y, double z, double w, double h, double d)
	{ return getBox(x, y - h / 2D, z, w, y + h / 2D, d); }
	
	public static AxisAlignedBB rotate90BoxV(AxisAlignedBB bb, int dir)
	{
		double x1 = bb.minX;
		double y1 = bb.minY;
		double z1 = bb.minZ;
		
		double x2 = bb.maxX;
		double y2 = bb.maxY;
		double z2 = bb.maxZ;
		
		if(dir < 0 || dir >= 6 || dir == 2 || dir == 3) return new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
		return new AxisAlignedBB(z1, y1, x1, z2, y2, x2);
	}
}