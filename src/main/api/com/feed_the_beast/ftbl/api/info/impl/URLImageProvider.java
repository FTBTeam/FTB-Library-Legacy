package com.feed_the_beast.ftbl.api.info.impl;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.info.IImageProvider;
import com.latmod.lib.InvalidTextureCoords;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 29.08.2016.
 */
public class URLImageProvider implements IImageProvider
{
    private final String URL;
    private ResourceLocation texture;

    public URLImageProvider(String url)
    {
        URL = url;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ResourceLocation getImage()
    {
        if(texture == null)
        {
            texture = new ResourceLocation(FTBLibFinals.MOD_ID, "url_images/" + URL.replace(':', '|'));
            FTBLibClient.getDownloadImage(texture, URL, InvalidTextureCoords.INSTANCE.getTexture(), null);
        }

        return texture;
    }
}
