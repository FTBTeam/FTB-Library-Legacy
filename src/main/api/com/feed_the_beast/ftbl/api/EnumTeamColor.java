package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.lib.EnumDyeColorHelper;
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
    BLUE("blue", EnumDyeColor.BLUE, TextFormatting.BLUE, 138),
    CYAN("cyan", EnumDyeColor.CYAN, TextFormatting.AQUA, 136),
    GREEN("green", EnumDyeColor.GREEN, TextFormatting.GREEN, 118),
    YELLOW("yellow", EnumDyeColor.YELLOW, TextFormatting.YELLOW, 131),
    ORANGE("orange", EnumDyeColor.ORANGE, TextFormatting.GOLD, 130),
    RED("red", EnumDyeColor.RED, TextFormatting.RED, 129),
    PINK("pink", EnumDyeColor.PINK, TextFormatting.LIGHT_PURPLE, 190),
    MAGENTA("magenta", EnumDyeColor.MAGENTA, TextFormatting.LIGHT_PURPLE, 143),
    PURPLE("purple", EnumDyeColor.PURPLE, TextFormatting.DARK_PURPLE, 141),
    GRAY("gray", EnumDyeColor.GRAY, TextFormatting.GRAY, 160);

    public static final EnumTeamColor[] VALUES = values();

    public static EnumTeamColor getFromIndex(int i)
    {
        if(i < 0 || i >= VALUES.length)
        {
            return BLUE;
        }

        return VALUES[i];
    }

    private final String name;
    private final EnumDyeColor dyeColor;
    private final TextFormatting textFormatting;
    private final byte colorID;

    EnumTeamColor(String n, EnumDyeColor d, TextFormatting t, int c)
    {
        name = n;
        dyeColor = d;
        textFormatting = t;
        colorID = (byte) c;
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

    public byte getColorID()
    {
        return colorID;
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