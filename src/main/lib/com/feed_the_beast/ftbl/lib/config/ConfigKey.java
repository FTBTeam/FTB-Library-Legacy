package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigValue;

/**
 * @author LatvianModder
 */
public class ConfigKey extends SimpleConfigKey
{
	private final IConfigValue defValue;
	private int flags;
	private String displayNameLangKey, group = "";

	public ConfigKey(String id, IConfigValue def, String group)
	{
		super(group.isEmpty() ? id : (group + "." + id));
		defValue = def;
		setGroup(group);
		setNameLangKey(getName());
	}

	public ConfigKey(String id, IConfigValue def)
	{
		this(id, def, "");
	}

	@Override
	public ConfigKey setNameLangKey(String key)
	{
		displayNameLangKey = key;
		return this;
	}

	@Override
	public ConfigKey setGroup(String g)
	{
		group = g;
		return this;
	}

	@Override
	public IConfigValue getDefValue()
	{
		return defValue;
	}

	@Override
	public int getFlags()
	{
		return flags;
	}

	public ConfigKey setFlags(int f)
	{
		flags = f;
		return this;
	}

	@Override
	public ConfigKey addFlags(int f)
	{
		flags |= f;
		return this;
	}

	@Override
	public String getNameLangKey()
	{
		return displayNameLangKey;
	}

	@Override
	public String getGroup()
	{
		return group;
	}
}