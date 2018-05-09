package com.feed_the_beast.ftblib.lib.util.misc;

import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import net.minecraft.util.IStringSerializable;

/**
 * @author LatvianModder
 */
public enum EnumIO implements IStringSerializable
{
	IO("io"),
	IN("in"),
	OUT("out"),
	NONE("none");

	public static final NameMap<EnumIO> NAME_MAP = NameMap.create(IO, NameMap.ObjectProperties.withTranslatedName(EnumIO::getLangKey), values());
	public static final String ENUM_LANG_KEY = "io_mode";

	private final String name;
	private final String langKey;

	EnumIO(String n)
	{
		name = n;
		langKey = "io_mode." + name;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public String getLangKey()
	{
		return langKey;
	}

	public Icon getIcon()
	{
		switch (this)
		{
			case IO:
				return GuiIcons.INV_IO;
			case IN:
				return GuiIcons.INV_IN;
			case OUT:
				return GuiIcons.INV_OUT;
			default:
				return GuiIcons.INV_NONE;
		}
	}

	public boolean canInsert()
	{
		return this == IO || this == IN;
	}

	public boolean canExtract()
	{
		return this == IO || this == OUT;
	}
}