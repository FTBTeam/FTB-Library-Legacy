package com.feed_the_beast.ftbl.api.config;

import com.latmod.lib.LMColor;
import gnu.trove.list.TIntList;
import gnu.trove.map.TByteObjectMap;
import gnu.trove.map.hash.TByteObjectHashMap;

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

    private static final TByteObjectMap<ConfigEntryType> TYPE_MAP = new TByteObjectHashMap<>();

    public final byte ID;

    ConfigEntryType(int i)
    {
        ID = (byte) i;
    }

    public static ConfigEntryType getFromID(byte id)
    {
        if(TYPE_MAP.isEmpty())
        {
            for(ConfigEntryType t : values())
            {
                TYPE_MAP.put(t.ID, t);
            }
        }

        return TYPE_MAP.get(id);
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