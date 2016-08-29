package com.latmod.lib;

import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 30.08.2016.
 */
public class WrappedTextureCoords implements ITextureCoords
{
    private ResourceLocation texture;

    public WrappedTextureCoords(ResourceLocation tex)
    {
        texture = tex;
    }

    @Override
    public ResourceLocation getTexture()
    {
        return texture;
    }

    @Override
    public double getMinU()
    {
        return 0D;
    }

    @Override
    public double getMinV()
    {
        return 0D;
    }

    @Override
    public double getMaxU()
    {
        return 1D;
    }

    @Override
    public double getMaxV()
    {
        return 1D;
    }

    @Override
    public boolean isValid()
    {
        return true;
    }
}
