package com.feed_the_beast.ftbl.api;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.IStringSerializable;

/**
 * Created by LatvianModder on 26.05.2016.
 */
public enum EnumTeamStatus implements IStringSerializable
{
    ENEMY(-1, EnumDyeColor.RED),
    NONE(0, EnumDyeColor.WHITE),
    ALLY(1, EnumDyeColor.LIGHT_BLUE),
    MEMBER(2, EnumDyeColor.LIME),
    OWNER(3, EnumDyeColor.ORANGE);

    public static final EnumTeamStatus[] VALUES = values();

    private final String name;
    private final int status;
    private final EnumDyeColor color;

    EnumTeamStatus(int s, EnumDyeColor c)
    {
        name = name().toLowerCase();
        status = s;
        color = c;
    }

    @Override
    public String getName()
    {
        return name;
    }

    public int getStatus()
    {
        return status;
    }

    public EnumDyeColor getColor()
    {
        return color;
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
}