package com.feed_the_beast.ftbl.lib.client;

import net.minecraft.util.ResourceLocation;

public final class TextureCoords extends ImageProvider
{
    public static TextureCoords fromUV(ResourceLocation res, double u0, double v0, double u1, double v1)
    {
        return new TextureCoords(res, u0, v0, u1, v1);
    }

    public static TextureCoords fromCoords(ResourceLocation res, int x, int y, int w, int h, int tw, int th)
    {
        return fromUV(res, x / (double) tw, y / (double) th, (x + w) / (double) tw, (y + h) / (double) th);
    }

    private final double minU, minV, maxU, maxV;

    private TextureCoords(ResourceLocation res, double u0, double v0, double u1, double v1)
    {
        super(res);
        minU = Math.min(u0, u1);
        minV = Math.min(v0, v1);
        maxU = Math.max(u0, u1);
        maxV = Math.max(v0, v1);
    }

    @Override
    public double getMinU()
    {
        return minU;
    }

    @Override
    public double getMinV()
    {
        return minV;
    }

    @Override
    public double getMaxU()
    {
        return maxU;
    }

    @Override
    public double getMaxV()
    {
        return maxV;
    }

    @Override
    public boolean isValid()
    {
        return maxU - minU > 0D && maxV - minV > 0D;
    }
}