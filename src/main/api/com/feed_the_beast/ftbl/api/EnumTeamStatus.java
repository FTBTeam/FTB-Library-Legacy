package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.lib.ILangKeyContainer;
import com.feed_the_beast.ftbl.lib.LangKey;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

/**
 * Created by LatvianModder on 26.05.2016.
 */
public enum EnumTeamStatus implements IStringSerializable, ILangKeyContainer
{
    ENEMY(-10, "enemy", TextFormatting.RED),
    NONE(0, "none", TextFormatting.WHITE),
    INVITED(1, "invited", TextFormatting.WHITE),
    ALLY(3, "ally", TextFormatting.BLUE),
    MEMBER(5, "member", TextFormatting.GREEN),
    OWNER(10, "owner", TextFormatting.GOLD);

    public static final EnumTeamStatus[] VALUES = values();

    private final String name;
    private final int status;
    private final TextFormatting color;
    private final LangKey langKey;

    EnumTeamStatus(int s, String n, TextFormatting c)
    {
        name = n;
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