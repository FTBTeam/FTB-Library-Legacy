package com.feed_the_beast.ftbl.api.config;

import net.minecraft.util.IStringSerializable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by LatvianModder on 05.06.2016.
 */
public class EnumNameMap<E extends Enum<E>>
{
    public final List<E> values;
    public final Map<String, E> map;

    public EnumNameMap(E[] v)
    {
        values = Collections.unmodifiableList(Arrays.asList(v));
        HashMap<String, E> map1 = new HashMap<>();

        for(E e : values)
        {
            map1.put(getEnumName(e), e);
        }

        map = Collections.unmodifiableMap(map1);
    }

    public static String getEnumName(Enum<?> e)
    {
        if(e == null)
        {
            return "-";
        }
        else if(e instanceof IStringSerializable)
        {
            return ((IStringSerializable) e).getName();
        }
        else
        {
            return e.name().toLowerCase(Locale.US);
        }
    }

    public E get(String s)
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
}