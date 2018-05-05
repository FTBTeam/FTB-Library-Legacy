package com.feed_the_beast.ftblib.lib.util.misc;

import com.feed_the_beast.ftblib.lib.ICustomName;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @author LatvianModder
 */
public enum EnumRedstoneMode implements IStringSerializable, ICustomName
{
	DISABLED("disabled"),
	ACTIVE_HIGH("active_high"),
	ACTIVE_LOW("active_low"),
	PULSE("pulse");

	public static final NameMap<EnumRedstoneMode> NAME_MAP = NameMap.create(DISABLED, DISABLED, ACTIVE_HIGH, ACTIVE_LOW);
	public static final NameMap<EnumRedstoneMode> NAME_MAP_WITH_PULSE = NameMap.create(DISABLED, DISABLED, ACTIVE_HIGH, ACTIVE_LOW, PULSE);
	public static final String ENUM_LANG_KEY = "ftblib.redstonemode";

	private final String langKey;
	private final String name;

	EnumRedstoneMode(String n)
	{
		name = n;
		langKey = "ftblib.redstonemode." + name;
	}

	public String getLangKey()
	{
		return langKey;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public ITextComponent getCustomDisplayName()
	{
		return new TextComponentTranslation(langKey);
	}

	public boolean isActive(boolean prevValue, boolean value)
	{
		switch (this)
		{
			case DISABLED:
				return false;
			case ACTIVE_HIGH:
				return value;
			case ACTIVE_LOW:
				return !value;
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