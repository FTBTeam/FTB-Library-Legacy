package com.feed_the_beast.ftbl.lib.block;

import com.feed_the_beast.ftbl.api.item.IMaterial;
import com.feed_the_beast.ftbl.api.item.IMetaLookup;
import gnu.trove.map.hash.TByteObjectHashMap;
import net.minecraft.block.properties.PropertyEnum;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by LatvianModder on 09.08.2016.
 */
public final class BlockVariantLookup<T extends Enum<T> & IMaterial> implements IMetaLookup<T>, Iterable<T>
{
    private final PropertyEnum<T> property;
    private final T defValue;
    private final TByteObjectHashMap<T> metaMap;

    public BlockVariantLookup(String id, Class<T> c, T def)
    {
        property = PropertyEnum.create(id, c);
        defValue = def;
        metaMap = new TByteObjectHashMap<>();

        for(T t : getValues())
        {
            metaMap.put((byte) t.getMetadata(), t);
        }
    }

    public BlockVariantLookup(String id, Class<T> c, T def, T[] values)
    {
        property = PropertyEnum.create(id, c, values);
        defValue = def;
        metaMap = new TByteObjectHashMap<>();

        for(T t : getValues())
        {
            metaMap.put((byte) t.getMetadata(), t);
        }
    }

    public PropertyEnum<T> getProperty()
    {
        return property;
    }

    @Override
    public Collection<T> getValues()
    {
        return property.getAllowedValues();
    }

    @Override
    public T get(int metadata)
    {
        T t = metaMap.get((byte) metadata);
        return (t == null) ? defValue : t;
    }

    @Override
    public Iterator<T> iterator()
    {
        return getValues().iterator();
    }
}