package com.feed_the_beast.ftblib.lib.math;

import net.minecraft.block.BlockLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
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

	public static final float[] NORMALS_X = new float[] {0F, 0F, 0F, 0F, -1F, 1F};
	public static final float[] NORMALS_Y = new float[] {-1F, 1F, 0F, 0F, 0F, 0F};
	public static final float[] NORMALS_Z = new float[] {0F, 0F, -1F, 1F, 0F, 0F};

	public static final int ROTATION_X[] = {90, 270, 0, 0, 0, 0};
	public static final int ROTATION_Y[] = {0, 0, 180, 0, 90, 270};

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

	public static double sq(double value)
	{
		return value * value;
	}

	public static double sqrt(double value)
	{
		return value == 0D || value == 1D ? value : Math.sqrt(value);
	}

	public static double sqrt2sq(double x, double y)
	{
		return sqrt(sq(x) + sq(y));
	}

	public static double sqrt3sq(double x, double y, double z)
	{
		return sqrt(sq(x) + sq(y) + sq(z));
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
		return sq(x2 - x1) + sq(y2 - y1);
	}

	public static double dist(double x1, double y1, double x2, double y2)
	{
		return sqrt(distSq(x1, y1, x2, y2));
	}

	public static int chunk(int i)
	{
		return i >> 4;
	}

	public static int chunk(double d)
	{
		return chunk(MathHelper.floor(d));
	}

	public static boolean canParseInt(@Nullable String string)
	{
		if (string == null || string.isEmpty())
		{
			return false;
		}

		try
		{
			Integer.parseInt(string);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	public static boolean canParseDouble(@Nullable String string)
	{
		if (string == null || string.isEmpty())
		{
			return false;
		}

		try
		{
			Double.parseDouble(string);
			return true;
		}
		catch (Exception ex)
		{
			return false;
		}
	}

	public static float lerp(float min, float max, float value)
	{
		return min + (max - min) * value;
	}

	public static double lerp(double min, double max, double value)
	{
		return min + (max - min) * value;
	}

	public static Vec3d lerp(double x1, double y1, double z1, double x2, double y2, double z2, double value)
	{
		return new Vec3d(lerp(x1, x2, value), lerp(y1, y2, value), lerp(z1, z2, value));
	}

	public static Vec3d lerp(Vec3d v1, Vec3d v2, double value)
	{
		return lerp(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z, value);
	}

	public static double map(double min1, double max1, double min2, double max2, double value)
	{
		return lerp(min2, max2, (value - min1) / (max1 - min1));
	}

	public static double mod(double i, double n)
	{
		i = i % n;
		return i < 0 ? i + n : i;
	}

	public static int mod(int i, int n)
	{
		i = i % n;
		return i < 0 ? i + n : i;
	}

	@Nullable
	public static RayTraceResult rayTrace(EntityPlayer player, boolean useLiquids)
	{
		Vec3d vec3d = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		double myawr = Math.toRadians(-player.rotationYaw);
		double mpitchr = Math.toRadians(-player.rotationPitch);
		double f4 = -Math.cos(mpitchr);
		double dist = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
		Vec3d vec3d1 = vec3d.addVector(Math.sin(myawr - Math.PI) * f4 * dist, Math.sin(mpitchr) * dist, Math.cos(myawr - Math.PI) * f4 * dist);
		return player.world.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
	}

	@Nullable
	public static RayTraceResult collisionRayTrace(BlockPos pos, Vec3d start, Vec3d end, Iterable<AxisAlignedBB> boxes)
	{
		RayTraceResult result = null;
		double dist = Double.POSITIVE_INFINITY;
		int i = 0;

		for (AxisAlignedBB aabb : boxes)
		{
			RayTraceResult r = collisionRayTrace(pos, start, end, aabb, i, null);

			if (r != null)
			{
				double d1 = r.hitVec.squareDistanceTo(start);
				if (d1 < dist)
				{
					result = r;
					dist = d1;
				}
			}

			i++;
		}

		return result;
	}

	@Nullable
	public static RayTraceResult collisionRayTrace(BlockPos pos, Vec3d start, Vec3d end, @Nullable AxisAlignedBB box, int subHit, @Nullable Object hitInfo)
	{
		if (box == null)
		{
			return null;
		}

		RayTraceResult result = box.offset(pos).calculateIntercept(start, end);

		if (result == null)
		{
			return null;
		}
		else
		{
			result = new RayTraceResult(RayTraceResult.Type.BLOCK, result.hitVec, result.sideHit, pos);
			result.subHit = subHit;
			result.hitInfo = hitInfo;
			return result;
		}
	}

	@Nullable
	public static RayTraceResult collisionRayTrace(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB box)
	{
		return collisionRayTrace(pos, start, end, box, -1, null);
	}

	public static AxisAlignedBB rotateAABB(AxisAlignedBB box, EnumFacing facing)
	{
		switch (facing)
		{
			case DOWN:
				return box;
			case UP:
				return new AxisAlignedBB(1D - box.minX, 1D - box.minY, 1D - box.minZ, 1D - box.maxX, 1D - box.maxY, 1D - box.maxZ);
			case NORTH:
				return new AxisAlignedBB(box.minX, box.minZ, box.minY, box.maxX, box.maxZ, box.maxY);
			case SOUTH:
			{
				box = rotateAABB(box, EnumFacing.NORTH);
				return new AxisAlignedBB(1D - box.minX, box.minY, 1D - box.minZ, 1D - box.maxX, box.maxY, 1D - box.maxZ);
			}
			case WEST:
				return rotateCW(rotateAABB(box, EnumFacing.SOUTH));
			case EAST:
				return rotateCW(rotateAABB(box, EnumFacing.NORTH));
			default:
				return box;
		}
	}

	public static AxisAlignedBB rotateCW(AxisAlignedBB box)
	{
		return new AxisAlignedBB(1D - box.minZ, box.minY, box.minX, 1D - box.maxZ, box.maxY, box.maxX);
	}

	public static AxisAlignedBB[] getRotatedBoxes(AxisAlignedBB box)
	{
		AxisAlignedBB[] boxes = new AxisAlignedBB[6];

		for (EnumFacing f : EnumFacing.VALUES)
		{
			boxes[f.ordinal()] = rotateAABB(box, f);
		}

		return boxes;
	}

	public static boolean intersects(int ax1, int ay1, int ax2, int ay2, int bx1, int by1, int bx2, int by2)
	{
		return ax1 < bx2 && ax2 > bx1 && ay1 < by2 && ay2 > by1;
	}

	public static boolean intersects(double ax1, double ay1, double ax2, double ay2, double bx1, double by1, double bx2, double by2)
	{
		return ax1 < bx2 && ax2 > bx1 && ay1 < by2 && ay2 > by1;
	}

	public static ChunkPos getSpiralPoint(int index)
	{
		int x = 0, z = 0, p = 1, ringIndex = 0;

		int s = (int) (Math.ceil(Math.sqrt(index)) + ((Math.ceil(Math.sqrt(index)) % 2 + 1) % 2));
		if (s > 1)
		{
			ringIndex = index - (s - 2) * (s - 2);
			p = s * s - (s - 2) * (s - 2);
		}

		int ri = (ringIndex + s / 2) % p;

		if (s > 1)
		{
			x = ri < (p / 4) ? ri : (ri <= (p / 4 * 2 - 1) ? (p / 4) : (ri <= (p / 4 * 3) ? ((p / 4 * 3) - ri) : 0));
		}

		if (s > 1)
		{
			z = ri < (p / 4) ? 0 : (ri <= (p / 4 * 2 - 1) ? (ri - (p / 4)) : (ri <= (p / 4 * 3) ? (p / 4) : (p - ri)));
		}

		return new ChunkPos(x - s / 2, z - s / 2);
	}
}