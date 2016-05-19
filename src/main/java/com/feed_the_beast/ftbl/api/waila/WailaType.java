package com.feed_the_beast.ftbl.api.waila;

import com.feed_the_beast.ftbl.api.tile.IWailaTile;

public enum WailaType
{
    STACK(IWailaTile.Stack.class),
    HEAD(IWailaTile.Head.class),
    BODY(IWailaTile.Body.class),
    TAIL(IWailaTile.Tail.class);

    public final Class<? extends IWailaTile> typeClass;

    WailaType(Class<? extends IWailaTile> c)
    {
        typeClass = c;
    }
}