package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.lib.EnumDyeColorHelper;
import com.feed_the_beast.ftbl.lib.EnumNameMap;
import com.feed_the_beast.ftbl.lib.ILangKeyContainer;
import com.feed_the_beast.ftbl.lib.LangKey;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

/**
 * Created by LatvianModder on 07.06.2016.
 */
public enum EnumTeamColor implements IStringSerializable, ILangKeyContainer
{
    BLUE("blue", EnumDyeColor.BLUE, TextFormatting.BLUE, 0x0094FF),
    CYAN("cyan", EnumDyeColor.CYAN, TextFormatting.AQUA, 0x00DDFF),
    GREEN("green", EnumDyeColor.GREEN, TextFormatting.GREEN, 0x23D34C),
    YELLOW("yellow", EnumDyeColor.YELLOW, TextFormatting.YELLOW, 0xFFD000),
    ORANGE("orange", EnumDyeColor.ORANGE, TextFormatting.GOLD, 0xFF9400),
    RED("red", EnumDyeColor.RED, TextFormatting.RED, 0xEA4B4B),
    PINK("pink", EnumDyeColor.PINK, TextFormatting.LIGHT_PURPLE, 0xE888C9),
    MAGENTA("magenta", EnumDyeColor.MAGENTA, TextFormatting.LIGHT_PURPLE, 0xFF007F),
    PURPLE("purple", EnumDyeColor.PURPLE, TextFormatting.DARK_PURPLE, 0xB342FF),
    GRAY("gray", EnumDyeColor.GRAY, TextFormatting.GRAY, 0xC0C0C0);

    public static final EnumTeamColor[] VALUES = values();
    public static final EnumNameMap<EnumTeamColor> NAME_MAP = new EnumNameMap<>(EnumTeamColor.values(), false);

    public static EnumTeamColor get(int i)
    {
        return i < 0 || i >= VALUES.length ? BLUE : VALUES[i];
    }

    private final String name;
    private final EnumDyeColor dyeColor;
    private final TextFormatting textFormatting;
    private final int color;

    EnumTeamColor(String n, EnumDyeColor d, TextFormatting t, int c)
    {
        name = n;
        dyeColor = d;
        textFormatting = t;
        color = 0xFF000000 | c;
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