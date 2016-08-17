package com.feed_the_beast.ftbl.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public interface IRegistry<K, V>
{
    @Nonnull
    V register(@Nonnull K key, @Nonnull V v);

    @Nullable
    V get(@Nonnull K key);

    @Nonnull
    Set<K> getKeys();

    @Nonnull
    Collection<V> getValues();

    @Nonnull
    Set<Map.Entry<K, V>> getEntrySet();

    int size();
}
