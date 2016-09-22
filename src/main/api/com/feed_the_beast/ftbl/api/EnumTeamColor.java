package com.feed_the_beast.ftbl.api;

import com.latmod.lib.EnumDyeColorHelper;
import com.latmod.lib.EnumNameMap;
import com.latmod.lib.ILangKeyContainer;
import com.latmod.lib.LangKey;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

/**
 * Created by LatvianModder on 07.06.2016.
 */
public enum EnumTeamColor implements IStringSerializable, ILangKeyContainer
{
    BLUE(EnumDyeColor.BLUE, TextFormatting.BLUE, 0x007FFF),
    CYAN(EnumDyeColor.CYAN, TextFormatting.AQUA, 0x00F2D1),
    GREEN(EnumDyeColor.GREEN, TextFormatting.GREEN, 0x3FD300),
    YELLOW(EnumDyeColor.YELLOW, TextFormatting.YELLOW, 0xFFD800),
    ORANGE(EnumDyeColor.ORANGE, TextFormatting.GOLD, 0xFF7F00),
    RED(EnumDyeColor.RED, TextFormatting.RED, 0xFF5E5E),
    PINK(EnumDyeColor.PINK, TextFormatting.LIGHT_PURPLE, 0xFF7CBD),
    MAGENTA(EnumDyeColor.MAGENTA, TextFormatting.LIGHT_PURPLE, 0xFF00FF),
    PURPLE(EnumDyeColor.PURPLE, TextFormatting.DARK_PURPLE, 0x7F00FF),
    GRAY(EnumDyeColor.GRAY, TextFormatting.GRAY, 0xC0C0C0);

    public static final EnumTeamColor[] VALUES = values();

    private final String name;
    private final EnumDyeColor dyeColor;
    private final TextFormatting textFormatting;
    private final int color;

    EnumTeamColor(EnumDyeColor d, TextFormatting t, int c)
    {
        name = EnumNameMap.createName(this);
        dyeColor = d;
        textFormatting = t;
        color = c;
    }

    @Override
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

    public EnumDyeColor getDyeColor()
    {
        return dyeColor;
    }

    @Override
    public LangKey getLangKey()
    {
        return EnumDyeColorHelper.get(getDyeColor()).getLangKey();
    }
}