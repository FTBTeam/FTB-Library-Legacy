package com.feed_the_beast.ftbl.lib.reg;

import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 17.09.2016.
 */
public class ResourceLocationIDRegistry extends IDRegistry<ResourceLocation>
{
    @Override
    public String createStringFromKey(ResourceLocation rl)
    {
        return rl.toString();
    }

    @Override
    public ResourceLocation createKeyFromString(String s)
    {
        return new ResourceLocation(s);
    }
}
