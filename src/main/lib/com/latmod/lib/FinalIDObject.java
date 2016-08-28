package com.latmod.lib;

import com.latmod.lib.util.LMStringUtils;
import net.minecraft.util.IStringSerializable;

public class FinalIDObject implements IStringSerializable
{
    private final String ID;

    public FinalIDObject(String id)
    {
        if(id.isEmpty())
        {
            throw new NullPointerException("ID can't be empty!");
        }

        ID = id;
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
        return o != null && (o == this || o == ID || ID.equals(LMStringUtils.getID(o)));
    }
}