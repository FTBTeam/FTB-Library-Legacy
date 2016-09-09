package com.feed_the_beast.ftbl.api.info.impl;

import com.feed_the_beast.ftbl.api.info.IImageProvider;
import com.latmod.lib.InvalidTextureCoords;
import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 30.08.2016.
 */
public enum EmptyImageProvider implements IImageProvider
{
    INSTANCE;

    @Override
    public ResourceLocation getImage()
    {
        return InvalidTextureCoords.INSTANCE.getTexture();
    }
}