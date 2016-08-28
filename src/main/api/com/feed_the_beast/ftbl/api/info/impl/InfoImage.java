package com.feed_the_beast.ftbl.api.info.impl;

import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 12.06.2016.
 */
public class InfoImage
{
    public static final InfoImage NULL = new InfoImage(new ResourceLocation("textures/misc/unknown_pack.png"), 64, 64);

    public final ResourceLocation texture;
    public final int width, height;

    public InfoImage(ResourceLocation rl, int w, int h)
    {
        texture = rl;
        width = w;
        height = h;
    }
}