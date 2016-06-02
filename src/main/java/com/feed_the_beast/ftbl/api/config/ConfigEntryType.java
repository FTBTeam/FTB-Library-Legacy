package com.feed_the_beast.ftbl.api.config;

import latmod.lib.IntList;
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

    public ConfigEntry createNew(String id)
    {
        switch(this)
        {
            case CUSTOM:
                return new ConfigEntryCustom(id);
            case GROUP:
                return new ConfigGroup(id);
            case BOOLEAN:
                return new ConfigEntryBool(id, false);
            case INT:
                return new ConfigEntryInt(id, 0);
            case DOUBLE:
                return new ConfigEntryDouble(id, 0D);
            case STRING:
                return new ConfigEntryString(id, null);
            case ENUM:
                return new ConfigEntryStringEnum(id);
            case INT_ARRAY:
                return new ConfigEntryIntList(id, (IntList) null);
            case STRING_ARRAY:
                return new ConfigEntryStringList(id, null);
            case COLOR:
                return new ConfigEntryColor(id, new LMColor.RGB());
            default:
                return null;
        }
    }
}