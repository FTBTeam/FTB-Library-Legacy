package com.feed_the_beast.ftbl.lib.math;

import net.minecraft.block.BlockLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Made by LatvianModder
 */
public class MathUtils
{
	public static final Random RAND = new Random();

	public static final double RAD = Math.PI / 180D;
	public static final double DEG = 180D / Math.PI;
	public static final double TWO_PI = Math.PI * 2D;
	public static final double HALF_PI = Math.PI / 2D;

	public static final float PI_F = (float) Math.PI;
	public static final float RAD_F = (float) RAD;
	public static final float DEG_F = (float) DEG;
	public static final float TWO_PI_F = (float) TWO_PI;
	public static final float HALF_PI_F = (float) HALF_PI;

	public static final float[] NORMALS_X = new float[] {0F, 0F, 0F, 0F, -1F, 1F};
	public static final float[] NORMALS_Y = new float[] {-1F, 1F, 0F, 0F, 0F, 0F};
	public static final float[] NORMALS_Z = new float[] {0F, 0F, -1F, 1F, 0F, 0F};

	public static final int FACING_BIT_DOWN = 1;
	public static final int FACING_BIT_UP = 2;
	public static final int FACING_BIT_NORTH = 4;
	public static final int FACING_BIT_SOUTH = 8;
	public static final int FACING_BIT_WEST = 16;
	public static final int FACING_BIT_EAST = 32;
	public static final int FACING_BIT[] = {FACING_BIT_DOWN, FACING_BIT_UP, FACING_BIT_NORTH, FACING_BIT_SOUTH, FACING_BIT_WEST, FACING_BIT_EAST};
	public static final int OPPOSITE[] = {1, 0, 3, 2, 5, 4};
	public static final int OPPOSITE_BIT[] = {2, 1, 8, 4, 32, 16};

	public static final int ROTATION_X[] = {90, 270, 0, 0, 0, 0};
	public static final int ROTATION_Y[] = {0, 0, 180, 0, 90, 270};

	@Nullable
	public static EnumFacing getFacing(int i)
	{
		return i < 0 || i > 5 ? null : EnumFacing.VALUES[i];
	}

	public static int getFacingIndex(@Nullable EnumFacing facing)
	{
		return facing == null ? -1 : facing.getIndex();
	}

	public static boolean isNumberBetween(int num, int num1, int num2)
	{
		int min = Math.min(num1, num2);
		int max = Math.max(num1, num2);
		return num >= min && num <= max;
	}

	public static BlockLog.EnumAxis getAxis(BlockPos pos1, BlockPos pos2)
	{
		int x = pos1.getX() - pos2.getX();
		int y = pos1.getY() - pos2.getY();
		int z = pos1.getZ() - pos2.getZ();

		if (x != 0 && y == 0 && z == 0)
		{
			return BlockLog.EnumAxis.X;
		}
		else if (x == 0 && y != 0 && z == 0)
		{
			return BlockLog.EnumAxis.Y;
		}
		else if (x == 0 && y == 0 && z != 0)
		{
			return BlockLog.EnumAxis.Z;
		}

		return BlockLog.EnumAxis.NONE;
	}

	public static boolean isPosBetween(BlockPos pos, BlockPos pos1, BlockPos pos2)
	{
		int posx = pos.getX();
		int posy = pos.getY();
		int posz = pos.getZ();

		int pos1x = pos1.getX();
		int pos1y = pos1.getY();
		int pos1z = pos1.getZ();

		if (posx == pos1x && posy == pos1y && posz == pos1z)
		{
			return true;
		}

		int pos2x = pos2.getX();
		int pos2y = pos2.getY();
		int pos2z = pos2.getZ();

		if (posx == pos2x && posy == pos2y && posz == pos2z)
		{
			return true;
		}

		int x = pos1x - pos2x;
		int y = pos1y - pos2y;
		int z = pos1z - pos2z;

		if (x != 0 && y == 0 && z == 0)
		{
			return posy == pos1y && posz == pos1z && isNumberBetween(posx, pos1x, pos2x);
		}
		else if (x == 0 && y != 0 && z == 0)
		{
			return posx == pos1x && posz == pos1z && isNumberBetween(posy, pos1y, pos2y);
		}
		else if (x == 0 && y == 0 && z != 0)
		{
			return posx == pos1x && posy == pos1y && isNumberBetween(posz, pos1z, pos2z);
		}

		return false;
	}

	@Nullable
	public static EnumFacing getFacing(BlockPos pos1, BlockPos pos2)
	{
		int x = pos2.getX() - pos1.getX();
		int y = pos2.getY() - pos1.getY();
		int z = pos2.getZ() - pos1.getZ();

		if (x != 0 && y == 0 && z == 0)
		{
			return x > 0 ? EnumFacing.EAST : EnumFacing.WEST;
		}
		else if (x == 0 && y != 0 && z == 0)
		{
			return y > 0 ? EnumFacing.UP : EnumFacing.DOWN;
		}
		else if (x == 0 && y == 0 && z != 0)
		{
			return z > 0 ? EnumFacing.SOUTH : EnumFacing.NORTH;
		}

		return null;
	}

	public static double sqrt(double d)
	{
		if (d == 0D)
		{
			return 0D;
		}
		else if (d == 1D)
		{
			return 1D;
		}
		else
		{
			return Math.sqrt(d);
		}
	}

	public static double sqrt2sq(double x, double y)
	{
		return sqrt(sq(x) + sq(y));
	}

	public static double sqrt3sq(double x, double y, double z)
	{
		return sqrt(sq(x) + sq(y) + sq(z));
	}

	public static double sq(double f)
	{
		return f * f;
	}

	public static double distSq(double x1, double y1, double z1, double x2, double y2, double z2)
	{
		return (x1 == x2 && y1 == y2 && z1 == z2) ? 0D : (sq(x2 - x1) + sq(y2 - y1) + sq(z2 - z1));
	}

	public static double dist(double x1, double y1, double z1, double x2, double y2, double z2)
	{
		return sqrt(distSq(x1, y1, z1, x2, y2, z2));
	}

	public static double distSq(double x1, double y1, double x2, double y2)
	{
		if (x1 == x2 && y1 == y2)
		{
			return 0D;
		}
		return (sq(x2 - x1) + sq(y2 - y1));
	}

	public static double dist(double x1, double y1, double x2, double y2)
	{
		return sqrt(distSq(x1, y1, x2, y2));
	}

	public static Vec3d getLook(float yaw, float pitch, float dist)
	{
		float f = MathHelper.cos(pitch * RAD_F);
		float x1 = MathHelper.cos(HALF_PI_F - yaw * RAD_F);
		float z1 = MathHelper.sin(HALF_PI_F - yaw * RAD_F);
		float y1 = MathHelper.sin(pitch * RAD_F);
		return new Vec3d(x1 * f * dist, y1 * dist, z1 * f * dist);
	}

	public static int chunk(int i)
	{
		return i >> 4;
	}

	public static int chunk(double d)
	{
		return chunk(MathHelper.floor(d));
	}

	public static boolean isRound(double d)
	{
		return Math.round(d) == d;
	}

	public static boolean canParseInt(@Nullable String s)
	{
		try
		{
			Integer.parseInt(s);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static boolean canParseDouble(@Nullable String s)
	{
		try
		{
			Double.parseDouble(s);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static int lerp_int(int i1, int i2, double f)
	{
		return i1 + (int) ((i2 - i1) * f);
	}

	public static double lerp_double(double f1, double f2, double f)
	{
		return f1 + (f2 - f1) * f;
	}

	public static double map(double val, double min1, double max1, double min2, double max2)
	{
		return min2 + (max2 - min2) * ((val - min1) / (max1 - min1));
	}

	public static int mapInt(int val, int min1, int max1, int min2, int max2)
	{
		return min2 + (max2 - min2) * ((val - min1) / (max1 - min1));
	}

	public static Vec3d getMidPoint(double x1, double y1, double z1, double x2, double y2, double z2, double p)
	{
		double x = x2 - x1;
		double y = y2 - y1;
		double z = z2 - z1;
		double d = sqrt(x * x + y * y + z * z);
		return new Vec3d(x1 + (x / d) * (d * p), y1 + (y / d) * (d * p), z1 + (z / d) * (d * p));
	}

	public static Vec3d getMidPoint(Vec3d v1, Vec3d v2, double p)
	{
		return getMidPoint(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z, p);
	}

	public static int getRotations(double yaw, int max)
	{
		return MathHelper.floor((yaw * max / 360D) + 0.5D) & (max - 1);
	}

	public static double wrap(double i, double n)
	{
		i = i % n;
		return (i < 0D) ? i + n : i;
	}

	public static int wrap(int i, int n)
	{
		i = i % n;
		return (i < 0) ? i + n : i;
	}

	public static Vec3d getEyePosition(EntityPlayer ep)
	{
		return new Vec3d(ep.posX, ep.world.isRemote ? ep.posY : (ep.posY + ep.getEyeHeight()), ep.posZ);
	}

	@Nullable
	public static RayTraceResult rayTrace(EntityPlayer playerIn, boolean useLiquids, double dist)
	{
		Vec3d vec3d = new Vec3d(playerIn.posX, playerIn.posY + playerIn.getEyeHeight(), playerIn.posZ);
		float f2 = MathHelper.cos(-playerIn.rotationYaw * RAD_F - PI_F);
		float f3 = MathHelper.sin(-playerIn.rotationYaw * RAD_F - PI_F);
		float f4 = -MathHelper.cos(-playerIn.rotationPitch * RAD_F);
		Vec3d vec3d1 = vec3d.addVector(f3 * f4 * dist, MathHelper.sin(-playerIn.rotationPitch * RAD_F) * dist, f2 * f4 * dist);
		return playerIn.world.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
	}

	@Nullable
	public static RayTraceResult rayTrace(EntityPlayer playerIn, boolean useLiquids)
	{
		double dist = 5D;

		if (playerIn instanceof EntityPlayerMP)
		{
			dist = ((EntityPlayerMP) playerIn).interactionManager.getBlockReachDistance();
		}

		return rayTrace(playerIn, useLiquids, dist);
	}

	@Nullable
	public static RayTraceResult collisionRayTrace(BlockPos blockPos, Vec3d start, Vec3d end, Iterable<AxisAlignedBB> boxes)
	{
		RayTraceResult current = null;
		double dist = Double.POSITIVE_INFINITY;
		int i = 0;

		for (AxisAlignedBB aabb : boxes)
		{
			if (aabb != null)
			{
				RayTraceResult mop = collisionRayTrace(blockPos, start, end, aabb);

				if (mop != null)
				{
					double d1 = mop.hitVec.squareDistanceTo(start);
					if (current == null || d1 < dist)
					{
						current = mop;
						current.subHit = i;
						dist = d1;
					}
				}
			}

			i++;
		}

		return current;
	}

	@Nullable
	public static RayTraceResult collisionRayTrace(BlockPos blockPos, Vec3d start, Vec3d end, AxisAlignedBB aabb)
	{
		Vec3d pos = start.addVector(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ());
		Vec3d rot = end.addVector(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ());

		Vec3d xmin = pos.getIntermediateWithXValue(rot, aabb.minX);
		Vec3d xmax = pos.getIntermediateWithXValue(rot, aabb.maxX);
		Vec3d ymin = pos.getIntermediateWithYValue(rot, aabb.minY);
		Vec3d ymax = pos.getIntermediateWithYValue(rot, aabb.maxY);
		Vec3d zmin = pos.getIntermediateWithZValue(rot, aabb.minZ);
		Vec3d zmax = pos.getIntermediateWithZValue(rot, aabb.maxZ);

		if (!isVecInsideYZBounds(xmin, aabb))
		{
			xmin = null;
		}
		if (!isVecInsideYZBounds(xmax, aabb))
		{
			xmax = null;
		}
		if (!isVecInsideXZBounds(ymin, aabb))
		{
			ymin = null;
		}
		if (!isVecInsideXZBounds(ymax, aabb))
		{
			ymax = null;
		}
		if (!isVecInsideXYBounds(zmin, aabb))
		{
			zmin = null;
		}
		if (!isVecInsideXYBounds(zmax, aabb))
		{
			zmax = null;
		}
		Vec3d v = null;

		if (xmin != null)// && (v == null || pos.squareDistanceTo(xmin) < pos.squareDistanceTo(v)))
		{
			v = xmin;
		}
		if (xmax != null && (v == null || pos.squareDistanceTo(xmax) < pos.squareDistanceTo(v)))
		{
			v = xmax;
		}
		if (ymin != null && (v == null || pos.squareDistanceTo(ymin) < pos.squareDistanceTo(v)))
		{
			v = ymin;
		}
		if (ymax != null && (v == null || pos.squareDistanceTo(ymax) < pos.squareDistanceTo(v)))
		{
			v = ymax;
		}
		if (zmin != null && (v == null || pos.squareDistanceTo(zmin) < pos.squareDistanceTo(v)))
		{
			v = zmin;
		}
		if (zmax != null && (v == null || pos.squareDistanceTo(zmax) < pos.squareDistanceTo(v)))
		{
			v = zmax;
		}
		if (v == null)
		{
			return null;
		}
		else
		{
			EnumFacing side = null;

			if (v == xmin)
			{
				side = EnumFacing.WEST;
			}
			if (v == xmax)
			{
				side = EnumFacing.EAST;
			}
			if (v == ymin)
			{
				side = EnumFacing.DOWN;
			}
			if (v == ymax)
			{
				side = EnumFacing.UP;
			}
			if (v == zmin)
			{
				side = EnumFacing.NORTH;
			}
			if (v == zmax)
			{
				side = EnumFacing.SOUTH;
			}

			return new RayTraceResult(v.addVector(blockPos.getX(), blockPos.getY(), blockPos.getZ()), side, blockPos);
		}
	}

	private static boolean isVecInsideYZBounds(@Nullable Vec3d v, AxisAlignedBB aabb)
	{
		return v != null && (v.y >= aabb.minY && v.y <= aabb.maxY && v.z >= aabb.minZ && v.z <= aabb.maxZ);
	}

	private static boolean isVecInsideXZBounds(@Nullable Vec3d v, AxisAlignedBB aabb)
	{
		return v != null && (v.x >= aabb.minX && v.x <= aabb.maxX && v.z >= aabb.minZ && v.z <= aabb.maxZ);
	}

	private static boolean isVecInsideXYBounds(@Nullable Vec3d v, AxisAlignedBB aabb)
	{
		return v != null && (v.x >= aabb.minX && v.x <= aabb.maxX && v.y >= aabb.minY && v.y <= aabb.maxY);
	}

	public static RayTraceResult getMOPFrom(BlockPos pos, EnumFacing s, float hitX, float hitY, float hitZ)
	{
		return new RayTraceResult(new Vec3d(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ), s, pos);
	}

	public static AxisAlignedBB rotateAABB(AxisAlignedBB bb, EnumFacing facing)
	{
		switch (facing)
		{
			case DOWN:
				return bb;
			case UP:
				return new AxisAlignedBB(1D - bb.minX, 1D - bb.minY, 1D - bb.minZ, 1D - bb.maxX, 1D - bb.maxY, 1D - bb.maxZ);
			case NORTH:
				return new AxisAlignedBB(bb.minX, bb.minZ, bb.minY, bb.maxX, bb.maxZ, bb.maxY);
			case SOUTH:
			{
				bb = rotateAABB(bb, EnumFacing.NORTH);
				return new AxisAlignedBB(1D - bb.minX, bb.minY, 1D - bb.minZ, 1D - bb.maxX, bb.maxY, 1D - bb.maxZ);
			}
			case WEST:
				return rotateCW(rotateAABB(bb, EnumFacing.SOUTH));
			case EAST:
				return rotateCW(rotateAABB(bb, EnumFacing.NORTH));
			default:
				return bb;
		}
	}

	public static AxisAlignedBB rotateCW(AxisAlignedBB bb)
	{
		return new AxisAlignedBB(1D - bb.minZ, bb.minY, bb.minX, 1D - bb.maxZ, bb.maxY, bb.maxX);
	}

	public static AxisAlignedBB[] getRotatedBoxes(AxisAlignedBB bb)
	{
		AxisAlignedBB[] boxes = new AxisAlignedBB[6];

		for (EnumFacing f : EnumFacing.VALUES)
		{
			boxes[f.ordinal()] = rotateAABB(bb, f);
		}

		return boxes;
	}

	public static RayTraceResult rayTrace(BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		Vec3d vec = new Vec3d(hitX, hitY, hitZ);
		vec.addVector(pos.getX(), pos.getY(), pos.getZ());
		return new RayTraceResult(vec, facing, pos);
	}

	public static boolean intersects(double ax1, double ay1, double ax2, double ay2, double bx1, double by1, double bx2, double by2)
	{
		return ax1 < bx2 && ax2 > bx1 && ay1 < by2 && ay2 > by1;
	}
}