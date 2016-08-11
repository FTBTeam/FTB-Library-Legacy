package com.latmod.lib.util;

import com.latmod.lib.RemoveFilter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by LatvianModder on 06.01.2016.
 */
@ParametersAreNonnullByDefault
public class LMListUtils
{
    public static String toString(Collection<?> c)
    {
        String[] s = toStringArray(c);
        if(s == null)
        {
            return null;
        }
        if(s.length == 0)
        {
            return "[ ]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        sb.append(' ');

        for(int i = 0; i < s.length; i++)
        {
            sb.append(s[i]);

            if(i != s.length - 1)
            {
                sb.append(',');
                sb.append(' ');
            }
        }

        sb.append(' ');
        sb.append(']');
        return sb.toString();
    }

    @Nonnull
    public static String[] toStringArray(Collection<?> c)
    {
        String[] s = new String[c.size()];
        if(s.length == 0)
        {
            return s;
        }
        int i = -1;
        for(Object o : c)
        {
            s[++i] = String.valueOf(o);
        }
        return s;
    }

    @Nonnull
    public static List<String> toStringList(Collection<?> c)
    {
        List<String> list = new ArrayList<>(c.size());
        if(c.isEmpty())
        {
            return list;
        }
        for(Object o : c)
        {
            list.add(String.valueOf(o));
        }
        return list;
    }

    @Nonnull
    public static int[] toHashCodeArray(Collection<?> c)
    {
        if(c.isEmpty())
        {
            return new int[0];
        }
        int[] s = new int[c.size()];
        int i = -1;
        for(Object o : c)
        {
            s[++i] = LMUtils.hashCodeOf(o);
        }
        return s;
    }

    public static void removeNullValues(List<?> list)
    {
        for(int i = list.size() - 1; i >= 0; i--)
        {
            if(list.get(i) == null)
            {
                list.remove(i);
            }
        }
    }

    public static <E> void removeAll(List<E> list, @Nullable RemoveFilter<E> f)
    {
        if(f == null)
        {
            list.clear();
        }
        else
        {
            for(int i = list.size() - 1; i >= 0; i--)
            {
                if(f.remove(list.get(i)))
                {
                    list.remove(i);
                }
            }
        }
    }

    @Nonnull
    public static <E> List<E> sortToNew(Collection<E> c, Comparator<? super E> comparator)
    {
        if(c.isEmpty())
        {
            return Collections.emptyList();
        }

        List<E> list = new ArrayList<>(c.size());
        list.addAll(c);
        Collections.sort(list, comparator);
        return list;
    }

    public static boolean trim(List<?> list, int t)
    {
        if(list.size() > t)
        {
            while(list.size() > t)
            {
                list.remove(t);
                t--;
            }

            return true;
        }

        return false;
    }

    @Nonnull
    public static <E> List<E> clone(Collection<E> c)
    {
        if(c.isEmpty())
        {
            return Collections.emptyList();
        }

        ArrayList<E> list1 = new ArrayList<>(c.size());
        list1.addAll(c);
        return list1;
    }

    public static boolean containsAny(Collection<?> c, Collection<?> c1)
    {
        for(Object o : c1)
        {
            if(c.contains(o))
            {
                return true;
            }
        }

        return false;
    }

    @Nonnull
    public static <E> List<E> flip(List<E> list)
    {
        if(list.isEmpty())
        {
            return list;
        }
        int s = list.size();
        ArrayList<E> al1 = new ArrayList<>(s);
        for(int i = 0; i < s; i++)
        {
            al1.add(list.get(s - i - 1));
        }
        return al1;
    }
}