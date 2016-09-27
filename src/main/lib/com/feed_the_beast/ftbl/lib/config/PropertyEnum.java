package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.lib.EnumNameMap;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 14.09.2016.
 */
public class PropertyEnum<E extends Enum<E>> extends PropertyEnumAbstract<E>
{
    private final EnumNameMap<E> nameMap;
    private E value;

    public PropertyEnum(EnumNameMap<E> nm, @Nullable E e)
    {
        nameMap = nm;
        value = e;
    }

    @Override
    public EnumNameMap<E> getNameMap()
    {
        return nameMap;
    }

    @Nullable
    @Override
    public E get()
    {
        return value;
    }

    @Override
    public void set(@Nullable E e)
    {
        value = e;
    }
}