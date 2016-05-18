package com.feed_the_beast.ftbl.api;

/**
 * Created by LatvianModder on 23.04.2016.
 */
public enum MouseButton
{
    LEFT(0),
    RIGHT(1),
    MIDDLE(2),
    OTHER(3);
    
    public final byte ID;
    
    MouseButton(int b)
    {
        ID = (byte) b;
    }
    
    public boolean isLeft()
    { return this == LEFT; }
    
    public boolean isRight()
    { return this == RIGHT; }
    
    public static MouseButton get(int i)
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
                return OTHER;
        }
    }
}