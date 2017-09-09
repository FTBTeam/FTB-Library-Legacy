package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import net.minecraft.util.IStringSerializable;

/**
 * @author LatvianModder
 */
public enum EnumRedstoneMode implements IStringSerializable
{
	DISABLED("disabled"),
	ACTIVE_HIGH("active_high"),
	ACTIVE_LOW("active_low"),
	PULSE("pulse");

	public static final NameMap<EnumRedstoneMode> NAME_MAP = NameMap.create(DISABLED, DISABLED, ACTIVE_HIGH, ACTIVE_LOW);
	public static final NameMap<EnumRedstoneMode> NAME_MAP_WITH_PULSE = NameMap.create(DISABLED, DISABLED, ACTIVE_HIGH, ACTIVE_LOW, PULSE);
	public static final LangKey ENUM_LANG_KEY = LangKey.of("ftbl.redstonemode");

	private final LangKey langKey;
	private final String name;

	EnumRedstoneMode(String n)
	{
		name = n;
		langKey = LangKey.of("ftbl.redstonemode." + name);
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

	public Icon getIcon()
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