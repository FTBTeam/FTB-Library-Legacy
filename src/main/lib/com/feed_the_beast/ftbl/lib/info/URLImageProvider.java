package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.info.IImageProvider;
import com.feed_the_beast.ftbl.lib.client.FTBLibClient;
import com.feed_the_beast.ftbl.lib.client.InvalidTextureCoords;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import net.minecraft.util.ResourceLocation;

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
    public ResourceLocation getImage()
    {
        if(texture == null)
        {
            texture = FTBLibFinals.get("url_images/" + URL.replace(':', '|'));
            FTBLibClient.getDownloadImage(texture, URL, InvalidTextureCoords.INSTANCE.getTexture(), null);
        }

        return texture;
    }
}
