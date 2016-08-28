package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.ISyncedRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public class SyncedRegistry<V> extends SimpleRegistry<ResourceLocation, V> implements ISyncedRegistry<V>
{
    private final IntIDRegistry intIDs;

    public SyncedRegistry(boolean overrides)
    {
        super(overrides);
        intIDs = new IntIDRegistry();
    }

    @Override
    public V register(ResourceLocation key, V v)
    {
        V v1 = super.register(key, v);

        if(v1 == v)
        {
            getOrCreateIDFromKey(key);
        }

        return v1;
    }

    @Nullable
    @Override
    public ResourceLocation getKeyFromID(int numID)
    {
        return intIDs.getKeyFromID(numID);
    }

    @Override
    public int getIDFromKey(ResourceLocation key)
    {
        return intIDs.getIDFromKey(key);
    }

    @Override
    public int getOrCreateIDFromKey(ResourceLocation key)
    {
        return intIDs.getOrCreateIDFromKey(key);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        return intIDs.serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        intIDs.deserializeNBT(nbt);
    }
}