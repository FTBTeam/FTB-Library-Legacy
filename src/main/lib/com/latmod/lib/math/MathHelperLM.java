package com.latmod.lib.math;

import com.feed_the_beast.ftbl.FTBLibMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

/**
 * Made by LatvianModder
 */
public class MathHelperLM
{
    public static final Random RAND = new Random();
    public static final DecimalFormat SMALL_DOUBLE_FORMATTER = new DecimalFormat("#0.00");

    public static final double RAD = Math.PI / 180D;
    public static final double DEG = 180D / Math.PI;
    public static final double TWO_PI = Math.PI * 2D;
    public static final double HALF_PI = Math.PI / 2D;
    public static final float PI_F = (float) Math.PI;
    public static final float TWO_PI_F = (float) TWO_PI;
    public static final float HALF_PI_F = (float) HALF_PI;
    public static final double SQRT_2 = Math.sqrt(2D);

    public static final float RAD_F = (float) RAD;
    public static final float DEG_F = (float) DEG;

    private static final int SIN_TABLE_SIZE = 65536;
    private static final int SIN_TABLE_SIZE_1 = SIN_TABLE_SIZE - 1;
    private static final double[] SIN_TABLE = new double[SIN_TABLE_SIZE];
    private static final double SIN_SCALE = SIN_TABLE_SIZE / TWO_PI;
    private static final double COS_SHIFT = SIN_TABLE_SIZE / 4D;

    static
    {
        double ds = TWO_PI / (double) SIN_TABLE_SIZE;
        for(int i = 0; i < SIN_TABLE_SIZE; i++)
        {
            SIN_TABLE[i] = Math.sin(i * ds);
        }
    }

    public static double sin(double d)
    {
        return SIN_TABLE[(int) (d * SIN_SCALE) & SIN_TABLE_SIZE_1];
    }

    public static double cos(double d)
    {
        return SIN_TABLE[(int) (d * SIN_SCALE + COS_SHIFT) & SIN_TABLE_SIZE_1];
    }

    public static double tan(double d)
    {
        return sin(d) / cos(d);
    }

    public static double sinFromDeg(double f)
    {
        return sin(f * RAD);
    }

    public static double cosFromDeg(double f)
    {
        return cos(f * RAD);
    }

    public static double tanFromDeg(double f)
    {
        return tan(f * RAD);
    }

    public static double sqrt(double d)
    {
        if(d == 0D)
        {
            return 0D;
        }
        else if(d == 1D)
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

    public static double power(double i, int n)
    {
        if(n == 2)
        {
            return i * i;
        }
        double i1 = 1D;
        for(int j = 0; j < n; j++)
        {
            i1 *= i;
        }
        return i1;
    }

    public static int power(int i, int n)
    {
        if(n == 2)
        {
            return i * i;
        }
        int i1 = 1;
        for(int j = 0; j < n; j++)
        {
            i1 *= i;
        }
        return i1;
    }

    public static long power(long i, int n)
    {
        if(n == 2)
        {
            return i * i;
        }
        long i1 = 1L;
        for(int j = 0; j < n; j++)
        {
            i1 *= i;
        }
        return i1;
    }

    public static double distSq(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        if(x1 == x2 && y1 == y2 && z1 == z2)
        {
            return 0D;
        }
        return (sq(x2 - x1) + sq(y2 - y1) + sq(z2 - z1));
    }

    public static double dist(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        return sqrt(distSq(x1, y1, z1, x2, y2, z2));
    }

    public static double distSq(double x1, double y1, double x2, double y2)
    {
        if(x1 == x2 && y1 == y2)
        {
            return 0D;
        }
        return (sq(x2 - x1) + sq(y2 - y1));
    }

    public static double dist(double x1, double y1, double x2, double y2)
    {
        return sqrt(distSq(x1, y1, x2, y2));
    }

    public static Vec3d getLook(double yaw, double pitch, double dist)
    {
        double f = cos(pitch * RAD);
        double x1 = cos(-yaw * RAD + HALF_PI);
        double z1 = sin(-yaw * RAD + HALF_PI);
        double y1 = sin(pitch * RAD);
        return new Vec3d(x1 * f * dist, y1 * dist, z1 * f * dist);
    }

    public static int floor(double d)
    {
        int i = (int) d;
        return d < (double) i ? i - 1 : i;
    }

    public static int ceil(double d)
    {
        int i = (int) d;
        return d > (double) i ? i + 1 : i;
    }

    public static int chunk(int i)
    {
        return i >> 4;
    }

    public static int unchunk(int i)
    {
        return i << 4;
    }

    public static int chunk(double d)
    {
        return chunk(floor(d));
    }

    public static int randomInt(Random r, int min, int max)
    {
        if(min == max)
        {
            return min;
        }
        if(min > max)
        {
            int min0 = min;
            min = max;
            max = min0;
        }
        return min + r.nextInt(max - min + 1);
    }

    public static double randomDouble(Random r, double min, double max)
    {
        if(min == max)
        {
            return min;
        }
        if(min > max)
        {
            double min0 = min;
            min = max;
            max = min0;
        }
        return min + r.nextDouble() * (max - min);
    }

    public static boolean isRound(double d)
    {
        return Math.round(d) == d;
    }

    public static int lerpInt(int i1, int i2, double f)
    {
        return i1 + (int) ((i2 - i1) * f);
    }

    public static double lerp(double f1, double f2, double f)
    {
        return f1 + (f2 - f1) * f;
    }

    public static double clamp(double n, double min, double max)
    {
        if(n < min)
        {
            return min;
        }
        if(n > max)
        {
            return max;
        }
        return n;
    }

    public static int clampInt(int n, int min, int max)
    {
        if(n < min)
        {
            return min;
        }
        if(n > max)
        {
            return max;
        }
        return n;
    }

    public static float clampFloat(float n, float min, float max)
    {
        if(n < min)
        {
            return min;
        }
        if(n > max)
        {
            return max;
        }
        return n;
    }

    public static double[] clamp(double[] d, double min, double max)
    {
        for(int i = 0; i < d.length; i++)
        {
            d[i] = clamp(d[i], min, max);
        }
        return d;
    }

    public static int[] clampInt(int[] i, int min, int max)
    {
        for(int j = 0; j < i.length; j++)
        {
            i[j] = clampInt(i[j], min, max);
        }
        return i;
    }

    public static String toSmallDouble(double d)
    {
        return SMALL_DOUBLE_FORMATTER.format(d);
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
        double d = Math.sqrt(x * x + y * y + z * z);
        return new Vec3d(x1 + (x / d) * (d * p), y1 + (y / d) * (d * p), z1 + (z / d) * (d * p));
    }

    public static Vec3d getMidPoint(Vec3d v1, Vec3d v2, double p)
    {
        return getMidPoint(v1.xCoord, v1.yCoord, v1.zCoord, v2.xCoord, v2.yCoord, v2.zCoord, p);
    }

    public static int getRotations(double yaw, int max)
    {
        return floor((yaw * max / 360D) + 0.5D) & (max - 1);
    }

    public static int getRotYaw(int rot)
    {
        if(rot == 2)
        {
            return 180;
        }
        else if(rot == 3)
        {
            return 0;
        }
        else if(rot == 4)
        {
            return 90;
        }
        else if(rot == 5)
        {
            return -90;
        }
        return 0;
    }

    public static int getRotPitch(int rot)
    {
        if(rot == 0)
        {
            return 90;
        }
        else if(rot == 1)
        {
            return -90;
        }
        return 0;
    }

    public static boolean inRange(double d, double min, double max)
    {
        return d >= min && d <= max;
    }

    public static int percent(double d, double max)
    {
        return (int) (d / max * 100D);
    }

    public static double wrap(double i, double n)
    {
        i = i % n;
        if(i < 0D)
        {
            i += n;
        }
        return i;
    }

    public static int wrap(int i, int n)
    {
        i = i % n;
        if(i < 0)
        {
            i += n;
        }
        return i;
    }

    public static boolean isPow2(int i)
    {
        return i != 0 && (i & i - 1) == 0;
    }

    // MathHelperMC

    public static Vec3d getEyePosition(EntityPlayer ep)
    {
        double y = 0D;
        if(!ep.worldObj.isRemote)
        {
            y = ep.getEyeHeight();
        }
        return new Vec3d(ep.posX, ep.posY + y, ep.posZ);
    }

    public static RayTraceResult rayTrace(EntityPlayer ep, double d)
    {
        if(ep == null)
        {
            return null;
        }
        Vec3d pos = getEyePosition(ep);
        Vec3d look = ep.getLookVec();
        Vec3d vec = pos.addVector(look.xCoord * d, look.yCoord * d, look.zCoord * d);
        RayTraceResult mop = ep.worldObj.rayTraceBlocks(pos, vec, false, true, false);
        if(mop != null && mop.hitVec == null)
        {
            mop.hitVec = new Vec3d(0D, 0D, 0D);
        }
        return mop;
    }

    public static RayTraceResult rayTrace(EntityPlayer ep)
    {
        return rayTrace(ep, FTBLibMod.proxy.getReachDist(ep));
    }

    public static RayTraceResult collisionRayTrace(World w, BlockPos blockPos, Vec3d start, Vec3d end, AxisAlignedBB[] boxes)
    {
        if(boxes == null || boxes.length <= 0)
        {
            return null;
        }

        RayTraceResult current = null;
        double dist = Double.POSITIVE_INFINITY;

        for(int i = 0; i < boxes.length; i++)
        {
            if(boxes[i] != null)
            {
                RayTraceResult mop = collisionRayTrace(w, blockPos, start, end, boxes[i]);

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

    public static RayTraceResult collisionRayTrace(World w, BlockPos blockPos, Vec3d start, Vec3d end, List<AxisAlignedBB> boxes)
    {
        AxisAlignedBB[] boxesa = new AxisAlignedBB[boxes.size()];
        for(int i = 0; i < boxesa.length; i++)
        {
            boxesa[i] = boxes.get(i).addCoord(0D, 0D, 0D);
        }
        return collisionRayTrace(w, blockPos, start, end, boxesa);
    }

    public static RayTraceResult collisionRayTrace(World w, BlockPos blockPos, Vec3d start, Vec3d end, AxisAlignedBB aabb)
    {
        Vec3d pos = start.addVector(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ());
        Vec3d rot = end.addVector(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ());

        Vec3d xmin = pos.getIntermediateWithXValue(rot, aabb.minX);
        Vec3d xmax = pos.getIntermediateWithXValue(rot, aabb.maxX);
        Vec3d ymin = pos.getIntermediateWithYValue(rot, aabb.minY);
        Vec3d ymax = pos.getIntermediateWithYValue(rot, aabb.maxY);
        Vec3d zmin = pos.getIntermediateWithZValue(rot, aabb.minZ);
        Vec3d zmax = pos.getIntermediateWithZValue(rot, aabb.maxZ);

        if(!isVecInsideYZBounds(xmin, aabb))
        {
            xmin = null;
        }
        if(!isVecInsideYZBounds(xmax, aabb))
        {
            xmax = null;
        }
        if(!isVecInsideXZBounds(ymin, aabb))
        {
            ymin = null;
        }
        if(!isVecInsideXZBounds(ymax, aabb))
        {
            ymax = null;
        }
        if(!isVecInsideXYBounds(zmin, aabb))
        {
            zmin = null;
        }
        if(!isVecInsideXYBounds(zmax, aabb))
        {
            zmax = null;
        }
        Vec3d v = null;

        if(xmin != null && (v == null || pos.squareDistanceTo(xmin) < pos.squareDistanceTo(v)))
        {
            v = xmin;
        }
        if(xmax != null && (v == null || pos.squareDistanceTo(xmax) < pos.squareDistanceTo(v)))
        {
            v = xmax;
        }
        if(ymin != null && (v == null || pos.squareDistanceTo(ymin) < pos.squareDistanceTo(v)))
        {
            v = ymin;
        }
        if(ymax != null && (v == null || pos.squareDistanceTo(ymax) < pos.squareDistanceTo(v)))
        {
            v = ymax;
        }
        if(zmin != null && (v == null || pos.squareDistanceTo(zmin) < pos.squareDistanceTo(v)))
        {
            v = zmin;
        }
        if(zmax != null && (v == null || pos.squareDistanceTo(zmax) < pos.squareDistanceTo(v)))
        {
            v = zmax;
        }
        if(v == null)
        {
            return null;
        }
        else
        {
            EnumFacing side = null;

            if(v == xmin)
            {
                side = EnumFacing.WEST;
            }
            if(v == xmax)
            {
                side = EnumFacing.EAST;
            }
            if(v == ymin)
            {
                side = EnumFacing.DOWN;
            }
            if(v == ymax)
            {
                side = EnumFacing.UP;
            }
            if(v == zmin)
            {
                side = EnumFacing.NORTH;
            }
            if(v == zmax)
            {
                side = EnumFacing.SOUTH;
            }

            return new RayTraceResult(v.addVector(blockPos.getX(), blockPos.getY(), blockPos.getZ()), side, blockPos);
        }
    }

    private static boolean isVecInsideYZBounds(Vec3d v, AxisAlignedBB aabb)
    {
        return v != null && (v.yCoord >= aabb.minY && v.yCoord <= aabb.maxY && v.zCoord >= aabb.minZ && v.zCoord <= aabb.maxZ);
    }

    private static boolean isVecInsideXZBounds(Vec3d v, AxisAlignedBB aabb)
    {
        return v != null && (v.xCoord >= aabb.minX && v.xCoord <= aabb.maxX && v.zCoord >= aabb.minZ && v.zCoord <= aabb.maxZ);
    }

    private static boolean isVecInsideXYBounds(Vec3d v, AxisAlignedBB aabb)
    {
        return v != null && (v.xCoord >= aabb.minX && v.xCoord <= aabb.maxX && v.yCoord >= aabb.minY && v.yCoord <= aabb.maxY);
    }

    public static RayTraceResult getMOPFrom(BlockPos pos, EnumFacing s, float hitX, float hitY, float hitZ)
    {
        return new RayTraceResult(new Vec3d(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ), s, pos);
    }

    public static AxisAlignedBB getBox(double cx, double y0, double cz, double w, double y1, double d)
    {
        return new AxisAlignedBB(cx - w / 2D, y0, cz - d / 2D, cx + w / 2D, y1, cz + d / 2D);
    }

    public static AxisAlignedBB centerBox(double x, double y, double z, double w, double h, double d)
    {
        return getBox(x, y - h / 2D, z, w, y + h / 2D, d);
    }

    public static AxisAlignedBB rotateAABB(AxisAlignedBB bb, EnumFacing facing)
    {
        if(bb == null)
        {
            return null;
        }

        switch(facing)
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

        for(EnumFacing f : EnumFacing.VALUES)
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
}