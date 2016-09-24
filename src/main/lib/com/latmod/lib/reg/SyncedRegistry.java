package com.latmod.lib.reg;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public class SyncedRegistry<K, V> extends SimpleRegistry<K, V>
{
    private final IDRegistry<K> IDs;

    public SyncedRegistry(IDRegistry<K> r, boolean overrides)
    {
        super(overrides);
        IDs = r;
    }

    public IDRegistry<K> getIDs()
    {
        return IDs;
    }

    @Nullable
    public V getFromIntID(short numID)
    {
        K key = getIDs().getKeyFromID(numID);
        return key == null ? null : get(key);
    }
}