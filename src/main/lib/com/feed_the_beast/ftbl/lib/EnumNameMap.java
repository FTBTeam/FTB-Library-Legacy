package com.feed_the_beast.ftbl.lib;

import net.minecraft.util.IStringSerializable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

        Map<String, E> map1 = new HashMap<>(size);

        for(E e : values)
        {
            map1.put(getEnumName(e), e);
        }

        map = Collections.unmodifiableMap(map1);
    }

    public static String createName(Enum<?> e)
    {
        return e.name().toLowerCase(Locale.ENGLISH);
    }

    public static String getEnumName(@Nullable Enum<?> e)
    {
        if(e == null)
        {
            return NULL_VALUE;
        }
        else if(e instanceof IStringSerializable)
        {
            return ((IStringSerializable) e).getName();
        }
        else
        {
            return createName(e);
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