package com.feed_the_beast.ftbl.api.info;

import com.latmod.lib.ITextureCoords;
import com.latmod.lib.ITextureCoordsProvider;
import com.latmod.lib.WrappedTextureCoords;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 29.08.2016.
 */
public interface IImageProvider extends ITextureCoordsProvider
{
    @SideOnly(Side.CLIENT)
    ResourceLocation getImage();

    @Override
    default ITextureCoords getTextureCoords()
    {
        return new WrappedTextureCoords(getImage());
    }
}