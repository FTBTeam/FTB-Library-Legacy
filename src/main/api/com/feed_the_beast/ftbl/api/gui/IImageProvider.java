package com.feed_the_beast.ftbl.api.gui;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 15.01.2017.
 */
public interface IImageProvider
{
    boolean isValid();

    default boolean isURL()
    {
        return false;
    }

    ResourceLocation getImage();

    @SideOnly(Side.CLIENT)
    ITextureObject bindTexture();

    default double getMinU()
    {
        return 0D;
    }

    default double getMinV()
    {
        return 0D;
    }

    default double getMaxU()
    {
        return 1D;
    }

    default double getMaxV()
    {
        return 1D;
    }
}