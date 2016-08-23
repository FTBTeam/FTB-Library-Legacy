package com.feed_the_beast.ftbl.api;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public interface IRegistry<K, V>
{
    V register(K key, V v);

    V get(K key);

    Set<K> getKeys();

    Collection<V> getValues();

    Set<Map.Entry<K, V>> getEntrySet();

    int size();
}
