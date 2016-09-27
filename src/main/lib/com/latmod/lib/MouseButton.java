package com.latmod.lib;

import com.feed_the_beast.ftbl.api.gui.IMouseButton;

/**
 * Created by LatvianModder on 23.04.2016.
 */
public class MouseButton implements IMouseButton
{
    public static final MouseButton LEFT = new MouseButton(0);
    public static final MouseButton RIGHT = new MouseButton(1);
    public static final MouseButton MIDDLE = new MouseButton(2);

    public final int ID;

    private MouseButton(int b)
    {
        ID = b;
    }

    public static IMouseButton get(int i)
    {
        switch(i)
        {
            case 0:
                return LEFT;
            case 1:
                return RIGHT;
            case 2:
                return MIDDLE;
            default:
                return new MouseButton(i);
        }
    }

    public int hashCode()
    {
        return ID;
    }

    @Override
    public boolean isLeft()
    {
        return ID == LEFT.ID;
    }

    @Override
    public boolean isRight()
    {
        return ID == RIGHT.ID;
    }

    @Override
    public boolean isMiddle()
    {
        return ID == MIDDLE.ID;
    }

    @Override
    public int getButtonID()
    {
        return ID;
    }
}