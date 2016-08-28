package com.latmod.lib.util;

import com.latmod.lib.EnumNameMap;
import net.minecraft.util.IStringSerializable;

/**
 * Created by LatvianModder on 23.01.2016.
 */
public enum EnumJsonID implements IStringSerializable
{
    NULL,
    ARRAY,
    OBJECT,
    STRING,
    BOOL,
    BYTE,
    SHORT,
    INT,
    LONG,
    FLOAT,
    DOUBLE;

    private final String name;

    EnumJsonID()
    {
        name = EnumNameMap.createName(this);
    }

    @Override
    public String getName()
    {
        return name;
    }
}