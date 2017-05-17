package com.feed_the_beast.ftbl.lib.client;

import com.feed_the_beast.ftbl.lib.math.MathUtils;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class CubeBuilder
{
    public static class Face
    {
        private EnumFacing facing;

        private Face(EnumFacing f)
        {
            facing = f;
        }
    }

    private final CachedVertexData data;
    private double minX, minY, minZ, maxX, maxY, maxZ;
    private double minU, minV, maxU, maxV;

    public CubeBuilder(VertexFormat f)
    {
        data = new CachedVertexData(GL11.GL_QUADS, f);
    }

    public CubeBuilder setSize(double x0, double y0, double z0, double x1, double y1, double z1)
    {
        minX = x0;
        minY = y0;
        minZ = z0;
        maxX = x1;
        maxY = y1;
        maxZ = z1;
        return this;
    }

    public CubeBuilder setSize(AxisAlignedBB aabb)
    {
        return setSize(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
    }

    public CubeBuilder setSizeWHD(double x, double y, double z, double w, double h, double d)
    {
        return setSize(x, y, z, x + w, y + h, z + d);
    }

    public CubeBuilder add(EnumFacing facing)
    {
        float normalX = MathUtils.NORMALS_X[facing.getIndex()];
        float normalY = MathUtils.NORMALS_Y[facing.getIndex()];
        float normalZ = MathUtils.NORMALS_Z[facing.getIndex()];

        switch(facing)
        {
            case DOWN:
                data.pos(minX, minY, minZ).tex(minU, minV).normal(normalX, normalY, normalZ);
                data.pos(maxX, minY, minZ).tex(maxU, minV).normal(normalX, normalY, normalZ);
                data.pos(maxX, minY, maxZ).tex(maxU, maxV).normal(normalX, normalY, normalZ);
                data.pos(minX, minY, maxZ).tex(minU, maxV).normal(normalX, normalY, normalZ);
                break;
            case UP:
                data.pos(minX, maxY, minZ).tex(minU, minV).normal(normalX, normalY, normalZ);
                data.pos(minX, maxY, maxZ).tex(minU, maxV).normal(normalX, normalY, normalZ);
                data.pos(maxX, maxY, maxZ).tex(maxU, maxV).normal(normalX, normalY, normalZ);
                data.pos(maxX, maxY, minZ).tex(maxU, minV).normal(normalX, normalY, normalZ);
                break;
            case NORTH:
                data.pos(minX, minY, minZ).tex(maxU, maxV).normal(normalX, normalY, normalZ);
                data.pos(minX, maxY, minZ).tex(maxU, minV).normal(normalX, normalY, normalZ);
                data.pos(maxX, maxY, minZ).tex(minU, minV).normal(normalX, normalY, normalZ);
                data.pos(maxX, minY, minZ).tex(minU, maxV).normal(normalX, normalY, normalZ);
                break;
            case SOUTH:
                data.pos(minX, minY, maxZ).tex(minU, maxV).normal(normalX, normalY, normalZ);
                data.pos(maxX, minY, maxZ).tex(maxU, maxV).normal(normalX, normalY, normalZ);
                data.pos(maxX, maxY, maxZ).tex(maxU, minV).normal(normalX, normalY, normalZ);
                data.pos(minX, maxY, maxZ).tex(minU, minV).normal(normalX, normalY, normalZ);
                break;
            case WEST:
                data.pos(minX, minY, minZ).tex(minU, maxV).normal(normalX, normalY, normalZ);
                data.pos(minX, minY, maxZ).tex(maxU, maxV).normal(normalX, normalY, normalZ);
                data.pos(minX, maxY, maxZ).tex(maxU, minV).normal(normalX, normalY, normalZ);
                data.pos(minX, maxY, minZ).tex(minU, minV).normal(normalX, normalY, normalZ);
                break;
            case EAST:
                data.pos(maxX, minY, minZ).tex(maxU, maxV).normal(normalX, normalY, normalZ);
                data.pos(maxX, maxY, minZ).tex(maxU, minV).normal(normalX, normalY, normalZ);
                data.pos(maxX, maxY, maxZ).tex(minU, minV).normal(normalX, normalY, normalZ);
                data.pos(maxX, minY, maxZ).tex(minU, maxV).normal(normalX, normalY, normalZ);
                break;
        }

        return this;
    }

    public CubeBuilder addAll()
    {
        for(EnumFacing facing : EnumFacing.VALUES)
        {
            add(facing);
        }

        return this;
    }

    public CubeBuilder addSides()
    {
        for(EnumFacing facing : EnumFacing.VALUES)
        {
            if(facing.getAxis() != EnumFacing.Axis.Y)
            {
                add(facing);
            }
        }

        return this;
    }

    public CachedVertexData build()
    {
        return data.copy();
    }

    public void setUV(double u0, double v0, double u1, double v1)
    {
        minU = u0;
        minV = v0;
        maxU = u1;
        maxV = v1;
    }
}