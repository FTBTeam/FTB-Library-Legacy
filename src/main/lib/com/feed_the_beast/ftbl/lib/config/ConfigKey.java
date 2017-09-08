package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.feed_the_beast.ftbl.lib.util.StringUtils;

/**
 * @author LatvianModder
 */
public class ConfigKey extends FinalIDObject
{
	public ConfigKey(String id)
	{
		super(id, StringUtils.FLAG_ID_FIX | StringUtils.FLAG_ID_ONLY_UNDERLINE | StringUtils.FLAG_ID_ONLY_UNDERLINE_OR_PERIOD);
	}

	/**
	 * Will be excluded from writing / reading from files
	 */
	public boolean isExcluded()
	{
		return false;
	}

	/**
	 * Will be hidden from config gui
	 */
	public boolean isHidden()
	{
		return false;
	}

	/**
	 * Will be visible in config gui, but uneditable
	 */
	public boolean cantEdit()
	{
		return false;
	}

	/**
	 * Use scroll bar on numbers whenever that is available
	 */
	public boolean useScrollBar()
	{
		return false;
	}

	public ConfigValue getDefValue()
	{
		return ConfigNull.INSTANCE;
	}

	public String getNameLangKey()
	{
		return "";
	}

	public String getGroup()
	{
		return "";
	}

	public final String getDisplayName()
	{
		String key = getNameLangKey();

		if (key.isEmpty())
		{
			key = getName();
		}

		return StringUtils.canTranslate(key) ? StringUtils.translate(key) : getName();
	}

	public final String getDisplayInfo()
	{
		String key = getNameLangKey();

		if (key.isEmpty())
		{
			key = getName();
		}

		key = key + ".tooltip";
		return StringUtils.canTranslate(key) ? StringUtils.translate(key) : "";
	}

	public ConfigKey setNameLangKey(String name)
	{
		return this;
	}

	public ConfigKey setGroup(String name)
	{
		return this;
	}
}