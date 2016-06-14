package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.config.EnumNameMap;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;

/**
 * Created by LatvianModder on 07.06.2016.
 */
public enum EnumTeamColor implements IStringSerializable
{
    BLUE("blue", TextFormatting.BLUE, 0x007FFF),
    CYAN("cyan", TextFormatting.AQUA, 0x00FFDD),
    GREEN("green", TextFormatting.GREEN, 0x00FF3F),
    YELLOW("yellow", TextFormatting.YELLOW, 0xFFD800),
    ORANGE("orange", TextFormatting.GOLD, 0xFF7F00),
    RED("red", TextFormatting.RED, 0xFF0000),
    PINK("pink", TextFormatting.LIGHT_PURPLE, 0xFF7CBD),
    MAGENTA("magenta", TextFormatting.LIGHT_PURPLE, 0xFF00FF),
    PURPLE("purple", TextFormatting.DARK_PURPLE, 0x7F00FF),
    GRAY("gray", TextFormatting.GRAY, 0xC0C0C0);

    public static final EnumNameMap<EnumTeamColor> NAME_MAP = new EnumNameMap<>(false, EnumTeamColor.values());

    public final String name;
    public final TextFormatting textFormatting;
    public final int color;
    public final LangKey langKey;

    EnumTeamColor(String n, TextFormatting t, int c)
    {
        name = n;
        textFormatting = t;
        color = c;
        langKey = new LangKey("ftbl.lang.team_color." + name);
    }

    @Nonnull
    @Override
    public String getName()
    {
        return name;
    }
}