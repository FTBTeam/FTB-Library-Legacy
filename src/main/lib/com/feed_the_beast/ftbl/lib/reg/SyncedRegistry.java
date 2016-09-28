package com.feed_the_beast.ftbl.lib.reg;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public class SyncedRegistry<K, V>
{
    private final IDRegistry<K> IDs;
    private final boolean allowOverrides;
    private final Map<K, V> map;
    private final Map<K, V> mapMirror;

    public SyncedRegistry(IDRegistry<K> r, boolean overrides)
    {
        IDs = r;
        allowOverrides = overrides;
        map = new HashMap<>();
        mapMirror = Collections.unmodifiableMap(map);
    }

    public IDRegistry<K> getIDs()
    {
        return IDs;
    }

    public Map<K, V> getMap()
    {
        return mapMirror;
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

    @Nullable
    public V getFromIntID(short numID)
    {
        K key = getIDs().getKeyFromID(numID);
        return key == null ? null : get(key);
    }
}