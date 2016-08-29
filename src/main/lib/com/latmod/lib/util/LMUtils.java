package com.latmod.lib.util;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Made by LatvianModder
 */
public class LMUtils
{
    public static final Comparator<Package> PACKAGE_COMPARATOR = (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName());

    public static <E> List<E> getObjects(@Nullable Class<E> type, Class<?> fields, @Nullable Object obj) throws IllegalAccessException
    {
        List<E> l = new ArrayList<>();

        for(Field f : fields.getDeclaredFields())
        {
            f.setAccessible(true);
            Object o = f.get(obj);

            if(type == null || type.isAssignableFrom(o.getClass()))
            {
                l.add((E) o);
            }
        }

        return l;
    }
}