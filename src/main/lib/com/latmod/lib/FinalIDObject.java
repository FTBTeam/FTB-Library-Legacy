package com.latmod.lib;

import com.latmod.lib.util.LMUtils;

import javax.annotation.Nonnull;

public class FinalIDObject implements IIDObject
{
    private final String ID;

    public FinalIDObject(@Nonnull String id)
    {
        if(id.isEmpty())
        {
            throw new NullPointerException("ID can't be empty!");
        }

        ID = id;
    }

    @Override
    @Nonnull
    public final String getID()
    {
        return ID;
    }

    @Override
    @Nonnull
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
        return o != null && (o == this || o == ID || ID.equals(LMUtils.getID(o)));
    }
}