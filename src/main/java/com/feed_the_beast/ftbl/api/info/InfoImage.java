package com.feed_the_beast.ftbl.api.info;

import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 12.06.2016.
 */
public class InfoImage
{
    public static final InfoImage NULL = new InfoImage(null, 0, 0);

    public final ResourceLocation texture;
    public final int width, height;

    public InfoImage(ResourceLocation rl, int w, int h)
    {
        texture = rl;
        width = w;
        height = h;
    }
}