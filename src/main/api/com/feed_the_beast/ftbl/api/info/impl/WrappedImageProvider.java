package com.feed_the_beast.ftbl.api.info.impl;

import com.feed_the_beast.ftbl.api.info.IImageProvider;
import net.minecraft.util.ResourceLocation;

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
    public ResourceLocation getImage()
    {
        return texture;
    }
}