package com.latmod.lib;

import com.google.common.base.Objects;
import net.minecraft.util.ResourceLocation;

public final class TextureCoords implements ITextureCoords
{
    public static TextureCoords fromUV(ResourceLocation res, double u0, double v0, double u1, double v1)
    {
        return new TextureCoords(res, u0, v0, u1, v1);
    }

    public static TextureCoords fromUV(ResourceLocation res)
    {
        return fromUV(res, 0D, 0D, 1D, 1D);
    }

    public static TextureCoords fromCoords(ResourceLocation res, int x, int y, int w, int h, int tw, int th)
    {
        return fromUV(res, x / (double) tw, y / (double) th, (x + w) / (double) tw, (y + h) / (double) th);
    }

    private final ResourceLocation texture;
    private final double minU, minV, maxU, maxV;

    private TextureCoords(ResourceLocation res, double u0, double v0, double u1, double v1)
    {
        texture = res;
        minU = Math.min(u0, u1);
        minV = Math.min(v0, v1);
        maxU = Math.max(u0, u1);
        maxV = Math.max(v0, v1);
    }

    @Override
    public ResourceLocation getTexture()
    {
        return texture;
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
    public int hashCode()
    {
        return Objects.hashCode(texture, minU, minV, maxU, maxV);
    }

    @Override
    public String toString()
    {
        return Double.toString(minU) + ',' + minV + ',' + maxU + ',' + maxV;
    }

    @Override
    public boolean isValid()
    {
        return texture != null && (maxU - minU) > 0D && (maxV - minV) > 0D;
    }

    public TextureCoords copy()
    {
        return new TextureCoords(texture, minU, minV, maxU, maxV);
    }
}