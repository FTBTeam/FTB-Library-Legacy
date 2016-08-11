package com.latmod.lib;

import net.minecraft.util.ResourceLocation;

import java.util.Comparator;

/**
 * Created by LatvianModder on 29.02.2016.
 */
public enum ResourceLocationComparator implements Comparator<ResourceLocation>
{
    INSTANCE;

    @Override
    public int compare(ResourceLocation o1, ResourceLocation o2)
    {
        int i = o1.getResourceDomain().compareToIgnoreCase(o2.getResourceDomain());
        return (i == 0) ? o1.getResourcePath().compareToIgnoreCase(o2.getResourcePath()) : i;
    }
}
