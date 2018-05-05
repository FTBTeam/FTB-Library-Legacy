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
public enum EnumIO implements IStringSerializable, ICustomName
{
	IO("io"),
	IN("in"),
	OUT("out"),
	NONE("none");

	public static final NameMap<EnumIO> NAME_MAP = NameMap.create(IO, values());
	public static final String ENUM_LANG_KEY = "ftblib.io";

	private final String name;
	private final String langKey;

	EnumIO(String n)
	{
		name = n;
		langKey = "ftblib.io." + name;
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