package com.feed_the_beast.ftblib.lib;

import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.util.EnumDyeColorHelper;
import com.feed_the_beast.ftblib.lib.util.misc.NameMap;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

/**
 * @author LatvianModder
 */
public enum EnumTeamColor implements IStringSerializable
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

	public static final NameMap<EnumTeamColor> NAME_MAP = NameMap.create(BLUE, NameMap.ObjectProperties.withNameAndColor((sender, value) -> new TextComponentTranslation(value.getLangKey()), EnumTeamColor::getColor), values());

	private final String name;
	private final EnumDyeColor dyeColor;
	private final TextFormatting textFormatting;
	private final Color4I color;
	private final String langKey;

	EnumTeamColor(String n, EnumDyeColor d, TextFormatting t, int c)
	{
		name = n;
		dyeColor = d;
		textFormatting = t;
		color = Color4I.rgb(c);
		langKey = EnumDyeColorHelper.get(dyeColor).getLangKey();
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

	public Color4I getColor()
	{
		return color;
	}

	public EnumDyeColor getDyeColor()
	{
		return dyeColor;
	}

	public String getLangKey()
	{
		return langKey;
	}
}