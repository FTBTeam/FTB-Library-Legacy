package com.latmod.lib.util;

import com.latmod.lib.RemoveFilter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 06.01.2016.
 */
@ParametersAreNonnullByDefault
public class LMMapUtils
{
    @Nonnull
    public static String toString(Map<?, ?> map)
    {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append(' ');

        int s = map.size();

        if(s > 0)
        {
            int i = 0;
            for(Map.Entry<?, ?> e : map.entrySet())
            {
                sb.append(e.getKey());
                sb.append(':');
                sb.append(' ');
                sb.append(e.getValue());

                if(i != s - 1)
                {
                    sb.append(',');
                    sb.append(' ');
                }
                i++;
            }

            sb.append(' ');
        }

        sb.append('}');
        return sb.toString();
    }

    @Nonnull
    public static Map<String, String> toStringMap(Map<?, ?> map)
    {
        Map<String, String> map1 = new HashMap<>();
        for(Map.Entry<?, ?> e : map.entrySet())
        {
            map1.put(String.valueOf(e.getKey()), String.valueOf(e.getValue()));
        }
        return map1;
    }

    @Nonnull
    public static <K, V> Map<V, K> inverse(Map<K, V> map)
    {
        Map<V, K> map1 = new HashMap<>();
        for(Map.Entry<K, V> e : map.entrySet())
        {
            map1.put(e.getValue(), e.getKey());
        }
        return map1;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static <K, V> List<Map.Entry<K, V>> sortedEntryList(Map<K, V> map, @Nullable Comparator<Map.Entry<K, V>> c)
    {
        List<Map.Entry<K, V>> list = new ArrayList<>();

        if(c == null)
        {
            c = (o1, o2) -> ((Comparable) o1.getKey()).compareTo(o2.getKey());
        }

        list.addAll(map.entrySet());
        Collections.sort(list, c);
        return list;
    }

    @Nonnull
    public static <K, V> List<V> values(Map<K, V> map, Comparator<Map.Entry<K, V>> c)
    {
        List<V> list = new ArrayList<>();
        for(Map.Entry<?, V> entry : sortedEntryList(map, c))
        {
            list.add(entry.getValue());
        }
        return list;
    }

    @Nonnull
    public static <K, V> Comparator<Map.Entry<K, V>> byKeyNames(final boolean ignoreCase)
    {
        return (o1, o2) ->
        {
            if(ignoreCase)
            {
                return String.valueOf(o1.getKey()).compareToIgnoreCase(String.valueOf(o2.getKey()));
            }
            else
            {
                return String.valueOf(o1.getKey()).compareTo(String.valueOf(o2.getKey()));
            }
        };
    }

    public static <K, V> void removeAll(Map<K, V> map, @Nullable RemoveFilter<Map.Entry<K, V>> f)
    {
        if(f == null)
        {
            map.clear();
        }
        else
        {
            List<Map.Entry<K, V>> set = new ArrayList<>(map.entrySet());
            map.clear();

            for(Map.Entry<K, V> e : set)
            {
                if(!f.remove(e))
                {
                    map.put(e.getKey(), e.getValue());
                }
            }
        }
    }

    public static <K, V> void sortMap(LinkedHashMap<K, V> map, Comparator<Map.Entry<K, V>> comparator)
    {
        if(!map.isEmpty())
        {
            List<Map.Entry<K, V>> list = new ArrayList<>();
            list.addAll(map.entrySet());
            Collections.sort(list, comparator);
            map.clear();

            for(Map.Entry<K, V> e : list)
            {
                map.put(e.getKey(), e.getValue());
            }
        }
    }
}