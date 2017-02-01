package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import net.minecraft.util.IStringSerializable;

public class FinalIDObject implements IStringSerializable
{
    private final String ID;

    public FinalIDObject(String id, int flags)
    {
        ID = LMStringUtils.getID(id, flags);
    }

    public FinalIDObject(String id)
    {
        this(id, LMStringUtils.FLAG_ID_DEFAULTS);
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
        return o == this || o == ID || (o != null && ID.equals(LMStringUtils.getID(o, LMStringUtils.FLAG_ID_FIX)));
    }
}