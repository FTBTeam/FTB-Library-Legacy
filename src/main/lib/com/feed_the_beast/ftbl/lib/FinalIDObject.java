package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.lib.util.StringUtils;
import net.minecraft.util.IStringSerializable;

public class FinalIDObject implements IStringSerializable
{
    private final String ID;

    public FinalIDObject(String id, int flags)
    {
        ID = StringUtils.getId(id, flags);
    }

    public FinalIDObject(String id)
    {
        this(id, StringUtils.FLAG_ID_DEFAULTS);
    }

    @Override
    public final String getName()
    {
        return ID;
    }

    @Override
    public String toString()
    {
        return ID;
    }

    @Override
    public final int hashCode()
    {
        return ID.hashCode();
    }

    @Override
    public final boolean equals(Object o)
    {
        return o == this || o == ID || (o != null && ID.equals(StringUtils.getId(o, StringUtils.FLAG_ID_FIX)));
    }
}