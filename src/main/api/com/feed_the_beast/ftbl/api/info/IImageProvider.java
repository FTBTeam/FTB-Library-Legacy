package com.feed_the_beast.ftbl.api.info;

import com.latmod.lib.client.ITextureCoords;
import com.latmod.lib.client.ITextureCoordsProvider;
import com.latmod.lib.client.WrappedTextureCoords;
import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 29.08.2016.
 */
public interface IImageProvider extends ITextureCoordsProvider
{
    ResourceLocation getImage();

    @Override
    default ITextureCoords getTextureCoords()
    {
        return new WrappedTextureCoords(getImage());
    }
}