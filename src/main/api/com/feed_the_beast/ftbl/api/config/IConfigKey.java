package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.lib.util.StringUtils;
import net.minecraft.util.IStringSerializable;

/**
 * @author LatvianModder
 */
public interface IConfigKey extends IStringSerializable
{
	/**
	 * Will be excluded from writing / reading from files
	 */
	int EXCLUDED = 1;

	/**
	 * Will be hidden from config gui
	 */
	int HIDDEN = 2;

	/**
	 * Will be visible in config gui, but uneditable
	 */
	int CANT_EDIT = 4;

	/**
	 * Use scroll bar on numbers whenever that is available
	 */
	int USE_SCROLL_BAR = 8;

	int getFlags();

	default boolean getFlag(int flag)
	{
		return (getFlags() & flag) != 0;
	}

	default IConfigKey addFlags(int flags)
	{
		return this;
	}

	IConfigValue getDefValue();

	default String getNameLangKey()
	{
		return "";
	}

	default String getGroup()
	{
		return "";
	}

	default String getDisplayName()
	{
		String key = getNameLangKey();

		if (key.isEmpty())
		{
			key = getName();
		}

		return StringUtils.canTranslate(key) ? StringUtils.translate(key) : getName();
	}

	default String getDisplayInfo()
	{
		String key = getNameLangKey();

		if (key.isEmpty())
		{
			key = getName() + ".tooltip";
		}

		return StringUtils.canTranslate(key) ? StringUtils.translate(key) : "";
	}

	default IConfigKey setNameLangKey(String name)
	{
		return this;
	}

	default IConfigKey setGroup(String name)
	{
		return this;
	}
}