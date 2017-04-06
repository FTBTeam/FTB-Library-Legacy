package com.feed_the_beast.ftbl.lib.client;

import com.feed_the_beast.ftbl.lib.Color4I;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    public final Color4I color;

    private CachedVertexData(int m, VertexFormat f, Collection<CachedVertex> oldList)
    {
        mode = m;
        format = f;
        list = new ArrayList<>(oldList);
        hasTex = f.hasUvOffset(0);
        hasColor = f.hasColor();
        hasNormal = f.hasNormal();
        color = new Color4I();
    }

    public CachedVertexData(int m, VertexFormat f)
    {
        this(m, f, Collections.emptyList());
    }

    public void reset()
    {
        list.clear();
    }

    public CachedVertexData copy()
    {
        return new CachedVertexData(mode, format, list);
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
        private int r = color.red(), g = color.green(), b = color.blue(), a = color.alpha();
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

    public void rect(int x, int y, int w, int h)
    {
        pos(x, y + h, 0D);
        pos(x + w, y + h, 0D);
        pos(x + w, y, 0D);
        pos(x, y, 0D);
    }

    public void rectWithTexture(int x, int y, int w, int h, double u0, double v0, double u1, double v1)
    {
        pos(x, y + h, 0D).tex(u0, v1);
        pos(x + w, y + h, 0D).tex(u1, v1);
        pos(x + w, y, 0D).tex(u1, v0);
        pos(x, y, 0D).tex(u0, v0);
    }
}