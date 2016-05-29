package com.feed_the_beast.ftbl.api;

/**
 * Created by LatvianModder on 14.05.2016.
 */
public enum EnumSelf
{
    SELF,
    OTHER;

    public boolean isSelf()
    {
        return this == SELF;
    }

    public boolean isOther()
    {
        return this == OTHER;
    }

    public boolean equalsType(EnumSelf t)
    {
        switch(t)
        {
            case SELF:
                return isSelf();
            case OTHER:
                return isOther();
            default:
                return true;
        }
    }
}