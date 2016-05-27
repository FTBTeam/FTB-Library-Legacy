package com.feed_the_beast.ftbl.api;

import net.minecraft.item.EnumDyeColor;

/**
 * Created by LatvianModder on 26.05.2016.
 */
public enum EnumTeamStatus
{
    ENEMY(-1, EnumDyeColor.RED),
    NONE(0, EnumDyeColor.WHITE),
    ALLY(1, EnumDyeColor.LIGHT_BLUE),
    MEMBER(2, EnumDyeColor.LIME),
    OWNER(3, EnumDyeColor.ORANGE);

    public static final EnumTeamStatus[] VALUES = values();

    public final int status;
    public final EnumDyeColor color;

    EnumTeamStatus(int s, EnumDyeColor c)
    {
        status = s;
        color = c;
    }

    public boolean isEnemy()
    {
        return status < 0;
    }

    public boolean isAlly()
    {
        return status > 0;
    }

    public boolean isMember()
    {
        return status >= MEMBER.status;
    }

    public boolean isOwner()
    {
        return this == OWNER;
    }

    public boolean isSpecialStatus()
    {
        return this == NONE || this == ENEMY || this == ALLY;
    }
}