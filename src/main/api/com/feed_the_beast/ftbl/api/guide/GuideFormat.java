package com.feed_the_beast.ftbl.api.guide;

import com.feed_the_beast.ftbl.lib.EnumNameMap;
import net.minecraft.util.IStringSerializable;

/**
 * Created by PC on 02.10.2016.
 */
public enum GuideFormat implements IStringSerializable
{
    JSON("json"),
    MD("md"),
    UNSUPPORTED("unsupported");

    private static final EnumNameMap<GuideFormat> NAME_MAP = new EnumNameMap<>(values(), false);

    private final String name;

    GuideFormat(String s)
    {
        name = s;
    }

    @Override
    public String getName()
    {
        return name;
    }

    public static GuideFormat getFromString(String s)
    {
        GuideFormat type = NAME_MAP.get(s);
        return (type == null) ? UNSUPPORTED : type;
    }
}