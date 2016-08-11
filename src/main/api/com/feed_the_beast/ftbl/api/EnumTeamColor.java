package com.feed_the_beast.ftbl.api;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;

/**
 * Created by LatvianModder on 07.06.2016.
 */
public enum EnumTeamColor implements IStringSerializable
{
    BLUE("blue", TextFormatting.BLUE, 0x007FFF),
    CYAN("cyan", TextFormatting.AQUA, 0x00F2D1),
    GREEN("green", TextFormatting.GREEN, 0x3FD300),
    YELLOW("yellow", TextFormatting.YELLOW, 0xFFD800),
    ORANGE("orange", TextFormatting.GOLD, 0xFF7F00),
    RED("red", TextFormatting.RED, 0xFF5E5E),
    PINK("pink", TextFormatting.LIGHT_PURPLE, 0xFF7CBD),
    MAGENTA("magenta", TextFormatting.LIGHT_PURPLE, 0xFF00FF),
    PURPLE("purple", TextFormatting.DARK_PURPLE, 0x7F00FF),
    GRAY("gray", TextFormatting.GRAY, 0xC0C0C0);

    private final String name;
    private final TextFormatting textFormatting;
    private final int color;
    private final LangKey langKey;

    EnumTeamColor(String n, TextFormatting t, int c)
    {
        name = n;
        textFormatting = t;
        color = c;
        langKey = new LangKey("ftbl.lang.team_color." + name);
    }

    @Override
    @Nonnull
    public String getName()
    {
        return name;
    }

    public TextFormatting getTextFormatting()
    {
        return textFormatting;
    }

    public int getColor()
    {
        return color;
    }

    public LangKey getLangKey()
    {
        return langKey;
    }
}