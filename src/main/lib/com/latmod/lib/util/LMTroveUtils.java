package com.latmod.lib.util;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by LatvianModder on 14.06.2016.
 */
@ParametersAreNonnullByDefault
public class LMTroveUtils
{
    public static void put(TIntIntMap map, int key, int value, int except)
    {
        if(value != except)
        {
            map.put(key, value);
        }
    }

    @Nonnull
    public static TIntList toIntList(TIntIntMap map)
    {
        TIntList list = new TIntArrayList();

        map.forEachEntry((key, value) ->
        {
            list.add(key);
            list.add(value);
            return true;
        });

        return list;
    }

    @Nonnull
    public static TIntIntMap fromArray(@Nullable int[] a)
    {
        TIntIntMap map = new TIntIntHashMap();

        if(a == null || a.length == 0)
        {
            return map;
        }

        for(int i = 0; i < a.length; i += 2)
        {
            map.put(a[i], a[i + 1]);
        }

        return map;
    }
}
