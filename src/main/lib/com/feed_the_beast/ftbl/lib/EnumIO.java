package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.api.ICustomName;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;

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
	public static final LangKey ENUM_LANG_KEY = FTBLibFinals.lang("io");

	private final String name;
	private final LangKey langKey;

	EnumIO(String n)
	{
		name = n;
		langKey = FTBLibFinals.lang("io." + name);
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public ITextComponent getCustomDisplayName()
	{
		return langKey.textComponent();
	}

	public LangKey getLangKey()
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