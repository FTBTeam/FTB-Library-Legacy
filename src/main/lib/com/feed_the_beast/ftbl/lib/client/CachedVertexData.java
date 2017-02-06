package com.feed_the_beast.ftbl.lib.client;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 05.02.2017.
 */
public class CachedVertexData
{
    private final int mode;
    private final VertexFormat format;
    private final List<CachedVertex> list;
    private final boolean hasTex, hasColor, hasNormal;

    public CachedVertexData(int m, VertexFormat f)
    {
        mode = m;
        format = f;
        list = new ArrayList<>();
        hasTex = f.hasUvOffset(0);
        hasColor = f.hasColor();
        hasNormal = f.hasNormal();
    }

    public CachedVertex pos(double x, double y, double z)
    {
        CachedVertex v = new CachedVertex(x, y, z);
        list.add(v);
        return v;
    }

    public CachedVertex pos(double x, double y)
    {
        return pos(x, y, 0D);
    }

    public void draw(Tessellator tessellator, VertexBuffer buffer)
    {
        if(list.isEmpty())
        {
            return;
        }

        buffer.begin(mode, format);
        for(CachedVertex v : list)
        {
            v.appendTo(buffer);
        }
        tessellator.draw();
    }

    public class CachedVertex
    {
        private double x, y, z, u, v;
        private int r, g, b, a;
        private float nx, ny, nz;

        private CachedVertex(double _x, double _y, double _z)
        {
            x = _x;
            y = _y;
            z = _z;
        }

        public CachedVertex tex(double _u, double _v)
        {
            u = _u;
            v = _v;
            return this;
        }

        public CachedVertex color(int _r, int _g, int _b, int _a)
        {
            r = _r;
            g = _g;
            b = _b;
            a = _a;
            return this;
        }

        public CachedVertex normal(float x, float y, float z)
        {
            nx = x;
            ny = y;
            nz = z;
            return this;
        }

        private void appendTo(VertexBuffer buffer)
        {
            buffer.pos(x, y, z);

            if(hasTex)
            {
                buffer.tex(u, v);
            }

            if(hasColor)
            {
                buffer.color(r, g, b, a);
            }

            if(hasNormal)
            {
                buffer.normal(nx, ny, nz);
            }

            buffer.endVertex();
        }
    }
}