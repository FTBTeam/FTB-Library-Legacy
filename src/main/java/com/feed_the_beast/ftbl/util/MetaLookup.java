package com.feed_the_beast.ftbl.util;

import com.feed_the_beast.ftbl.api.item.IMaterial;
import gnu.trove.map.hash.TByteObjectHashMap;

/**
 * Created by LatvianModder on 09.08.2016.
 */
public final class MetaLookup<T extends IMaterial>
{
    private final T[] values;
    private final T defValue;
    private final TByteObjectHashMap<T> metaMap;

    public MetaLookup(T[] val, T def)
    {
        values = val;
        defValue = def;
        metaMap = new TByteObjectHashMap<>();

        for(T t : values)
        {
            metaMap.put((byte) t.getMetadata(), t);
        }
    }

    public T[] getValues()
    {
        return values;
    }

    public T getDefaultValue()
    {
        return defValue;
    }

    public T get(int metadata)
    {
        T t = metaMap.get((byte) metadata);
        return (t == null) ? defValue : t;
    }
}