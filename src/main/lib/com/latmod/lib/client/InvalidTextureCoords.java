package com.latmod.lib.client;

import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 30.08.2016.
 */
public enum InvalidTextureCoords implements ITextureCoords
{
    INSTANCE;

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/misc/unknown_pack.png");

    @Override
    public ResourceLocation getTexture()
    {
        return TEXTURE;
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
        return false;
    }
}
