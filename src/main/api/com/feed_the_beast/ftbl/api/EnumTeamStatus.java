package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.lib.ILangKeyContainer;
import com.feed_the_beast.ftbl.lib.LangKey;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

/**
 * Created by LatvianModder on 26.05.2016.
 */
public enum EnumTeamStatus implements IStringSerializable, ILangKeyContainer
{
    ENEMY(-1, EnumDyeColor.RED),
    NONE(0, EnumDyeColor.WHITE),
    ALLY(1, EnumDyeColor.LIGHT_BLUE),
    MEMBER(2, EnumDyeColor.LIME),
    OWNER(3, EnumDyeColor.ORANGE);

    private final String name;
    private final int status;
    private final EnumDyeColor color;
    private final LangKey langKey;

    EnumTeamStatus(int s, EnumDyeColor c)
    {
        name = name().toLowerCase(Locale.ENGLISH);
        status = s;
        color = c;
        langKey = new LangKey("ftbl.lang.team_status." + name);
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

    @Override
    public LangKey getLangKey()
    {
        return langKey;
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