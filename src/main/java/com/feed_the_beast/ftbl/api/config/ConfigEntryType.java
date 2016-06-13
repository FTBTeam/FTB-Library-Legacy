package com.feed_the_beast.ftbl.api.config;

import gnu.trove.list.TIntList;
import latmod.lib.LMColor;

/**
 * Created by LatvianModder on 30.03.2016.
 */
public enum ConfigEntryType
{
    CUSTOM(1),
    GROUP(2),
    BOOLEAN(3),
    INT(4),
    DOUBLE(5),
    STRING(6),
    ENUM(7),
    INT_ARRAY(8),
    STRING_ARRAY(9),
    COLOR(10);

    private static final ConfigEntryType[] types = new ConfigEntryType[32];

    static
    {
        for(ConfigEntryType t : values())
        {
            types[t.ID] = t;
        }
    }

    public final byte ID;

    ConfigEntryType(int i)
    {
        ID = (byte) i;
    }

    public static ConfigEntryType getFromID(byte id)
    {
        return types[id];
    }

    public ConfigEntry createNew()
    {
        switch(this)
        {
            case CUSTOM:
                return new ConfigEntryCustom();
            case GROUP:
                return new ConfigGroup();
            case BOOLEAN:
                return new ConfigEntryBool(false);
            case INT:
                return new ConfigEntryInt(0);
            case DOUBLE:
                return new ConfigEntryDouble(0D);
            case STRING:
                return new ConfigEntryString(null);
            case ENUM:
                return new ConfigEntryStringEnum();
            case INT_ARRAY:
                return new ConfigEntryIntList((TIntList) null);
            case STRING_ARRAY:
                return new ConfigEntryStringList(null);
            case COLOR:
                return new ConfigEntryColor(new LMColor.RGB());
            default:
                return null;
        }
    }
}