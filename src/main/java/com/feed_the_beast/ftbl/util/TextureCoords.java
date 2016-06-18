package com.feed_the_beast.ftbl.util;

import com.latmod.lib.util.LMUtils;
import net.minecraft.util.ResourceLocation;

public final class TextureCoords
{
    public static final TextureCoords NULL_TEXTURE = new TextureCoords(null, 0D, 0D, 1D, 1D);

    public final ResourceLocation texture;
    public final double minU, minV, maxU, maxV;

    public TextureCoords(ResourceLocation res, double u0, double v0, double u1, double v1)
    {
        texture = res;
        minU = Math.min(u0, u1);
        minV = Math.min(v0, v1);
        maxU = Math.max(u0, u1);
        maxV = Math.max(v0, v1);
    }

    public TextureCoords(ResourceLocation res, double x, double y, double w, double h, double tw, double th)
    {
        texture = res;
        minU = x / tw;
        minV = y / th;
        maxU = (x + w) / tw;
        maxV = (y + h) / th;
    }

    public static TextureCoords getSquareIcon(ResourceLocation res)
    {
        return new TextureCoords(res, 0D, 0D, 1D, 1D);
    }

    @Override
    public int hashCode()
    {
        return LMUtils.hashCode(texture, minU, minV, maxU, maxV);
    }

    @Override
    public String toString()
    {
        return Double.toString(minU) + ',' + minV + ',' + maxU + ',' + maxV;
    }

    public boolean isValid()
    {
        return texture != null && this != NULL_TEXTURE && (maxU - minU) > 0D && (maxV - minV) > 0D;
    }

    public TextureCoords copy()
    {
        return new TextureCoords(texture, minU, minV, maxU, maxV);
    }
}