package com.feed_the_beast.ftbl.api;

import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public interface ISyncedRegistry<V> extends IRegistry<ResourceLocation, V>, IIntIDRegistry
{
}