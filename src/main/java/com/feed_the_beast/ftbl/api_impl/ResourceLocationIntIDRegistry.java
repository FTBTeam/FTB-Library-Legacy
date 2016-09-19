package com.feed_the_beast.ftbl.api_impl;

import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 17.09.2016.
 */
public class ResourceLocationIntIDRegistry extends AbstractIntIDRegistry<ResourceLocation>
{
    @Override
    public String createFromKey(ResourceLocation rl)
    {
        return rl.toString();
    }

    @Override
    public ResourceLocation createFromString(String s)
    {
        return new ResourceLocation(s);
    }
}
