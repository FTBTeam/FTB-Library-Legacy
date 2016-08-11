package com.latmod.lib.json;

/**
 * Created by LatvianModder on 23.01.2016.
 */
public enum EnumJsonID
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

    public final byte ID;
    public final String name;

    EnumJsonID()
    {
        ID = (byte) ordinal();
        name = name().toLowerCase();
    }
}