package com.latmod.lib;

import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 29.08.2016.
 */
public interface ITextureCoords
{
    ResourceLocation getTexture();

    double getMinU();

    double getMinV();

    double getMaxU();

    double getMaxV();

    boolean isValid();
}
