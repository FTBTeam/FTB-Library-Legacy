package com.feed_the_beast.ftbl.api.info;

import com.feed_the_beast.ftbl.lib.client.ITextureCoords;
import com.feed_the_beast.ftbl.lib.client.ITextureCoordsProvider;
import com.feed_the_beast.ftbl.lib.client.WrappedTextureCoords;
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