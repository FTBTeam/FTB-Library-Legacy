package com.feed_the_beast.ftbl.api.info.impl;

import com.feed_the_beast.ftbl.api.info.IImageProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 12.06.2016.
 */
public class WrappedImageProvider implements IImageProvider
{
    private final ResourceLocation texture;

    public WrappedImageProvider(ResourceLocation tex)
    {
        texture = tex;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ResourceLocation getImage()
    {
        return texture;
    }
}