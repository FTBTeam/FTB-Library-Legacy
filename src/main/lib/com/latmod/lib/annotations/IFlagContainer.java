package com.latmod.lib.annotations;

import com.latmod.lib.io.Bits;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public interface IFlagContainer extends IAnnotationContainer
{
    int getFlags();

    void setFlags(int flags);

    default void setFlag(int flag, boolean v)
    {
        setFlags(Bits.setFlag(getFlags(), flag, v));
    }

    default boolean getFlag(int flag)
    {
        return Bits.getFlag(getFlags(), flag);
    }
}
