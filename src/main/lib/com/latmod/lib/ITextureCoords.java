package com.latmod.lib;

import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 29.08.2016.
 */
public interface ITextureCoords extends ITextureCoordsProvider
{
    ResourceLocation getTexture();

    double getMinU();

    double getMinV();

    double getMaxU();

    double getMaxV();

    boolean isValid();

    @Override
    default ITextureCoords getTextureCoords()
    {
        return this;
    }
}