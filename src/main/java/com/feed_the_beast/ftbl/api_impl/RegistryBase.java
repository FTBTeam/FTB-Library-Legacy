package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.IRegistry;
import com.latmod.lib.util.LMListUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by LatvianModder on 26.07.2016.
 */
public final class RegistryBase<K, V> implements IRegistry<K, V>
{
    public static final int LINKED = 1;
    public static final int ALLOW_OVERRIDES = 2;

    private final int flags;
    private final Map<K, V> map;

    public RegistryBase(int f)
    {
        flags = f;
        map = ((flags & LINKED) != 0) ? new LinkedHashMap<>() : new HashMap<>();
    }

    @Override
    @Nonnull
    public V register(@Nonnull K key, @Nonnull V v)
    {
        if(((flags & ALLOW_OVERRIDES) != 0) || !map.containsKey(key))
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
    public List<String> getKeyStringList()
    {
        return LMListUtils.toStringList(getKeys());
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
