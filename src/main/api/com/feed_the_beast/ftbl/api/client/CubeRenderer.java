package com.feed_the_beast.ftbl.api.client;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3i;

public final class CubeRenderer
{
    private static final float[] normalsX = new float[] {0F, 0F, 0F, 0F, -1F, 1F};
    private static final float[] normalsY = new float[] {-1F, 1F, 0F, 0F, 0F, 0F};
    private static final float[] normalsZ = new float[] {0F, 0F, -1F, 1F, 0F, 0F};
    private final boolean hasTexture, hasNormals;
    public final Color4I color;
    private final VertexFormat format;
    private boolean beginAndEnd = true;
    private Tessellator tessellator;
    private VertexBuffer buffer;
    private double minX, minY, minZ, maxX, maxY, maxZ;
    private double minU, minV, maxU, maxV;

    public CubeRenderer(boolean tex, boolean norm)
    {
        hasTexture = tex;
        hasNormals = norm;

        color = new Color4I();
        format = new VertexFormat();
        format.addElement(DefaultVertexFormats.POSITION_3F);
        format.addElement(DefaultVertexFormats.COLOR_4UB);

        if(hasTexture)
        {
            format.addElement(DefaultVertexFormats.TEX_2F);
        }

        if(hasNormals)
        {
            format.addElement(DefaultVertexFormats.NORMAL_3B);
            format.addElement(DefaultVertexFormats.PADDING_1B);
        }
    }

    public CubeRenderer setTessellator(Tessellator t, VertexBuffer vb)
    {
        tessellator = t;
        buffer = vb;
        return this;
    }

    public CubeRenderer setTessellator(Tessellator t)
    {
        return setTessellator(t, t.getBuffer());
    }

    public void setSize(double x0, double y0, double z0, double x1, double y1, double z1)
    {
        minX = x0;
        minY = y0;
        minZ = z0;
        maxX = x1;
        maxY = y1;
        maxZ = z1;
    }

    public void setSize(AxisAlignedBB aabb)
    {
        setSize(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
    }

    public void setSizeWHD(double x, double y, double z, double w, double h, double d)
    {
        setSize(x, y, z, x + w, y + h, z + d);
    }

    public void offset(double x, double y, double z)
    {
        minX += x;
        minY += y;
        minZ += z;
        maxX += x;
        maxY += y;
        maxZ += z;
    }

    public void offset(Vec3i pos)
    {
        offset(pos.getX(), pos.getY(), pos.getZ());
    }

    public void setUV(double u0, double v0, double u1, double v1)
    {
        minU = u0;
        minV = v0;
        maxU = u1;
        maxV = v1;
    }

    public void renderAll()
    {
        begin();
        beginAndEnd = false;
        renderDown();
        renderUp();
        renderSouth();
        renderNorth();
        renderWest();
        renderEast();
        beginAndEnd = true;
        end();
    }

    public void renderSides()
    {
        begin();
        beginAndEnd = false;
        renderSouth();
        renderNorth();
        renderWest();
        renderEast();
        beginAndEnd = true;
        end();
    }

    public void renderFace(EnumFacing f)
    {
        switch(f)
        {
            case DOWN:
                renderDown();
                break;
            case UP:
                renderUp();
                break;
            case SOUTH:
                renderSouth();
                break;
            case NORTH:
                renderNorth();
                break;
            case WEST:
                renderWest();
                break;
            case EAST:
                renderEast();
                break;
        }
    }

    public void begin()
    {
        if(beginAndEnd)
        {
            buffer.begin(7, format);
        }
    }

    public void end()
    {
        if(beginAndEnd)
        {
            tessellator.draw();
        }
    }

    private void vertex(int i, double x, double y, double z, double u, double v)
    {
        buffer.pos(x, y, z);
        buffer.color(color.red, color.green, color.blue, color.alpha);

        if(hasTexture)
        {
            buffer.tex(u, v);
        }

        if(hasNormals)
        {
            buffer.normal(normalsX[i], normalsY[i], normalsZ[i]);
        }

        buffer.endVertex();
    }

    public void renderDown()
    {
        begin();
        vertex(0, minX, minY, minZ, minU, minV);
        vertex(0, maxX, minY, minZ, maxU, minV);
        vertex(0, maxX, minY, maxZ, maxU, maxV);
        vertex(0, minX, minY, maxZ, minU, maxV);
        end();
    }

    public void renderUp()
    {
        begin();
        vertex(1, minX, maxY, minZ, minU, minV);
        vertex(1, minX, maxY, maxZ, minU, maxV);
        vertex(1, maxX, maxY, maxZ, maxU, maxV);
        vertex(1, maxX, maxY, minZ, maxU, minV);
        end();
    }

    public void renderSouth()
    {
        begin();
        vertex(2, minX, minY, maxZ, minU, maxV);
        vertex(2, maxX, minY, maxZ, maxU, maxV);
        vertex(2, maxX, maxY, maxZ, maxU, minV);
        vertex(2, minX, maxY, maxZ, minU, minV);
        end();
    }

    public void renderNorth()
    {
        begin();
        vertex(3, minX, minY, minZ, maxU, maxV);
        vertex(3, minX, maxY, minZ, maxU, minV);
        vertex(3, maxX, maxY, minZ, minU, minV);
        vertex(3, maxX, minY, minZ, minU, maxV);
        end();
    }

    public void renderWest()
    {
        begin();
        vertex(4, minX, minY, minZ, minU, maxV);
        vertex(4, minX, minY, maxZ, maxU, maxV);
        vertex(4, minX, maxY, maxZ, maxU, minV);
        vertex(4, minX, maxY, minZ, minU, minV);
        end();
    }

    public void renderEast()
    {
        begin();
        vertex(5, maxX, minY, minZ, maxU, maxV);
        vertex(5, maxX, maxY, minZ, maxU, minV);
        vertex(5, maxX, maxY, maxZ, minU, minV);
        vertex(5, maxX, minY, maxZ, minU, maxV);
        end();
    }
}