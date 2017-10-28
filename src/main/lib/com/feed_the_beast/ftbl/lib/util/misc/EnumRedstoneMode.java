package com.feed_the_beast.ftbl.lib.util.misc;

import com.feed_the_beast.ftbl.api.ICustomName;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.util.LangKey;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;

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
	public static final LangKey ENUM_LANG_KEY = FTBLibFinals.lang("redstonemode");

	private final LangKey langKey;
	private final String name;

	EnumRedstoneMode(String n)
	{
		name = n;
		langKey = FTBLibFinals.lang("redstonemode." + name);
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

	@Override
	public ITextComponent getCustomDisplayName()
	{
		return langKey.textComponent(null);
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