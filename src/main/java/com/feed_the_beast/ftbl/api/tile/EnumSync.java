package com.feed_the_beast.ftbl.api.tile;

/**
 * Created by LatvianModder on 26.07.2016.
 */
public enum EnumSync
{
    OFF,
    SYNC,
    RERENDER;

    boolean sync()
    {
        return this == SYNC || this == RERENDER;
    }

    boolean rerender()
    {
        return this == RERENDER;
    }
}
