package com.feed_the_beast.ftbl.api;

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
public final class RegistryBase<K, V>
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

    @Nullable
    public V get(@Nonnull K key)
    {
        return map.get(key);
    }

    @Nonnull
    public Set<K> getKeys()
    {
        return map.keySet();
    }

    @Nonnull
    public List<String> getKeyStringList()
    {
        return LMListUtils.toStringList(getKeys());
    }

    @Nonnull
    public Collection<V> getValues()
    {
        return map.values();
    }

    @Nonnull
    public Set<Map.Entry<K, V>> getEntrySet()
    {
        return map.entrySet();
    }

    public int size()
    {
        return map.size();
    }
}
