package com.feed_the_beast.ftbl.api;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 17.08.2016.
 */
public interface IIntIDRegistry extends ISyncData
{
    @Nullable
    ResourceLocation getKeyFromID(int numID);

    int getIDFromKey(ResourceLocation key);

    int getOrCreateIDFromKey(ResourceLocation key);
}