package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.IRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by LatvianModder on 26.07.2016.
 */
public class SimpleRegistry<K, V> implements IRegistry<K, V>
{
    private final boolean allowOverrides;
    private final Map<K, V> map;

    public SimpleRegistry(boolean overrides)
    {
        allowOverrides = overrides;
        map = new HashMap<>();
    }

    @Override
    @Nonnull
    public V register(@Nonnull K key, @Nonnull V v)
    {
        if(allowOverrides || !map.containsKey(key))
        {
            map.put(key, v);
            return v;
        }

        return map.get(key);
    }

    @Override
    @Nullable
    public V get(@Nonnull K key)
    {
        return map.get(key);
    }

    @Override
    @Nonnull
    public Set<K> getKeys()
    {
        return map.keySet();
    }

    @Override
    @Nonnull
    public Collection<V> getValues()
    {
        return map.values();
    }

    @Override
    @Nonnull
    public Set<Map.Entry<K, V>> getEntrySet()
    {
        return map.entrySet();
    }

    @Override
    public int size()
    {
        return map.size();
    }
}
