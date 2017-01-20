package com.feed_the_beast.ftbl.lib;

import net.minecraft.util.IStringSerializable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by LatvianModder on 05.06.2016.
 */
public final class EnumNameMap<E extends Enum<E>>
{
    public static final String NULL_VALUE = "-";
    public final int size;
    private final Map<String, E> map;
    private final List<E> values;

    public EnumNameMap(E[] v, boolean addNull)
    {
        List<E> list = new ArrayList<>();

        for(E e : v)
        {
            if(e != null)
            {
                list.add(e);
            }
        }

        if(addNull)
        {
            list.add(null);
        }

        values = Collections.unmodifiableList(list);
        size = values.size();

        Map<String, E> map1 = new LinkedHashMap<>(size);

        for(E e : values)
        {
            map1.put(getName(e), e);
        }

        map = Collections.unmodifiableMap(map1);
    }

    public static String getName(@Nullable Object o)
    {
        if(o == null)
        {
            return NULL_VALUE;
        }
        else if(o instanceof IStringSerializable)
        {
            return ((IStringSerializable) o).getName();
        }
        else
        {
            return ((o instanceof Enum<?>) ? ((Enum<?>) o).name() : o.toString()).toLowerCase();
        }
    }

    @Nullable
    public E get(@Nullable String s)
    {
        if(s == null || s.isEmpty() || s.charAt(0) == '-')
        {
            return null;
        }
        else
        {
            return map.get(s);
        }
    }

    @Nullable
    public E getFromIndex(int index)
    {
        return values.get(index);
    }

    public int getIndex(@Nullable Object e)
    {
        return values.indexOf(e);
    }

    public int getStringIndex(String s)
    {
        return getIndex(map.get(s));
    }

    public Set<String> getKeys()
    {
        return map.keySet();
    }

    public List<E> getValues()
    {
        return values;
    }
}