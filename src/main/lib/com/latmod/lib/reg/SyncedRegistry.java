package com.latmod.lib.reg;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public class SyncedRegistry<K, V> extends SimpleRegistry<K, V>
{
    private final IntIDRegistry<K> intIDs;

    public SyncedRegistry(IntIDRegistry<K> r, boolean overrides)
    {
        super(overrides);
        intIDs = r;
    }

    public IntIDRegistry<K> getIntIDs()
    {
        return intIDs;
    }

    @Nullable
    public V getFromIntID(int numID)
    {
        K key = getIntIDs().getKeyFromID(numID);
        return key == null ? null : get(key);
    }
}