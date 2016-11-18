package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.lib.ILangKeyContainer;
import com.feed_the_beast.ftbl.lib.LangKey;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

import java.util.Locale;

/**
 * Created by LatvianModder on 26.05.2016.
 */
public enum EnumTeamStatus implements IStringSerializable, ILangKeyContainer
{
    ENEMY(-1, TextFormatting.RED),
    NONE(0, TextFormatting.WHITE),
    ALLY(1, TextFormatting.BLUE),
    MEMBER(2, TextFormatting.GREEN),
    OWNER(3, TextFormatting.GOLD);

    private final String name;
    private final int status;
    private final TextFormatting color;
    private final LangKey langKey;

    EnumTeamStatus(int s, TextFormatting c)
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

    public TextFormatting getColor()
    {
        return color;
    }

    @Override
    public LangKey getLangKey()
    {
        return langKey;
    }
}