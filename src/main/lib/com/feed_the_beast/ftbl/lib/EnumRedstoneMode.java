package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import net.minecraft.util.IStringSerializable;

public enum EnumRedstoneMode implements IStringSerializable
{
	DISABLED("disabled"),
	ACTIVE_HIGH("active_high"),
	ACTIVE_LOW("active_low"),
	PULSE("pulse");

	public static final EnumRedstoneMode[] VALUES = {DISABLED, ACTIVE_HIGH, ACTIVE_LOW};
	public static final EnumRedstoneMode[] VALUES_WITH_PULSE = {DISABLED, ACTIVE_HIGH, ACTIVE_LOW, PULSE};
	public static final LangKey ENUM_LANG_KEY = new LangKey("ftbl.redstonemode");

	private final LangKey langKey;
	private final String name;

	EnumRedstoneMode(String n)
	{
		name = n;
		langKey = new LangKey("ftbl.redstonemode." + name);
	}

	public LangKey getLangKey()
	{
		return langKey;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public boolean isActive(boolean rsHigh)
	{
		switch (this)
		{
			case DISABLED:
				return false;
			case ACTIVE_HIGH:
				return rsHigh;
			case ACTIVE_LOW:
				return !rsHigh;
			default:
				return false;
		}
	}

	public IDrawableObject getIcon()
	{
		switch (this)
		{
			case ACTIVE_HIGH:
				return GuiIcons.RS_HIGH;
			case ACTIVE_LOW:
				return GuiIcons.RS_LOW;
			case PULSE:
				return GuiIcons.RS_PULSE;
			default:
				return GuiIcons.RS_NONE;
		}
	}
}