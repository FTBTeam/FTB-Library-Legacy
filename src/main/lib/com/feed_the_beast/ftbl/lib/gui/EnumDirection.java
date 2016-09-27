package com.feed_the_beast.ftbl.lib.gui;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public enum EnumDirection
{
    HORIZONTAL,
    VERTICAL;

    public boolean isHorizontal()
    {
        return this == HORIZONTAL;
    }

    public boolean isVertical()
    {
        return this == VERTICAL;
    }
}