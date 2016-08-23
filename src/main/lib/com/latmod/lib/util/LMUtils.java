package com.latmod.lib.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Made by LatvianModder
 */
@ParametersAreNonnullByDefault
public class LMUtils
{
    public static final Comparator<Package> PACKAGE_COMPARATOR = (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName());

    @SuppressWarnings("all")
    @Nonnull
    public static <E> E newObject(Class<?> c, Object... o) throws Exception
    {
        if(o != null && o.length > 0)
        {
            Class<?>[] params = new Class<?>[o.length];
            for(int i = 0; i < o.length; i++)
            {
                params[i] = o.getClass();
            }

            Constructor<?> c1 = c.getConstructor(params);
            return (E) c1.newInstance(o);
        }

        return (E) c.newInstance();
    }

    public static boolean areObjectsEqual(@Nullable Object o1, @Nullable Object o2, boolean allowNulls)
    {
        return (o1 == null && o2 == null) ? allowNulls : (!(o1 == null || o2 == null) && (o1 == o2 || o1.equals(o2)));
    }

    public static int hashCodeOf(@Nullable Object o)
    {
        return o == null ? 0 : o.hashCode();
    }

    public static int hashCode(@Nullable Object... o)
    {
        if(o == null || o.length == 0)
        {
            return 0;
        }
        else if(o.length == 1)
        {
            return hashCodeOf(o[0]);
        }
        else
        {
            return Arrays.hashCode(o);
        }
    }

    public static long longHashCode(@Nullable Object... o)
    {
        if(o == null || o.length == 0)
        {
            return 0L;
        }
        else if(o.length == 1)
        {
            return hashCodeOf(o[0]);
        }

        long h = 1L;
        for(Object anO : o)
        {
            h = h * 31L + hashCodeOf(anO);
        }

        return h;
    }

    @Nonnull
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