package com.latmod.lib.reg;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public class SyncedRegistry<K, V>
{
    private final IDRegistry<K> IDs;
    private final boolean allowOverrides;
    private final Map<K, V> map;

    public SyncedRegistry(IDRegistry<K> r, boolean overrides)
    {
        IDs = r;
        allowOverrides = overrides;
        map = new HashMap<>();
    }

    public IDRegistry<K> getIDs()
    {
        return IDs;
    }

    public V register(K key, V v)
    {
        if(allowOverrides || !map.containsKey(key))
        {
            map.put(key, v);
            return v;
        }

        return map.get(key);
    }

    @Nullable
    public V get(K key)
    {
        return map.get(key);
    }

    public Set<K> getKeys()
    {
        return map.keySet();
    }

    public Collection<V> getValues()
    {
        return map.values();
    }

    public Set<Map.Entry<K, V>> getEntrySet()
    {
        return map.entrySet();
    }

    public int size()
    {
        return map.size();
    }

    @Nullable
    public V getFromIntID(short numID)
    {
        K key = getIDs().getKeyFromID(numID);
        return key == null ? null : get(key);
    }
}