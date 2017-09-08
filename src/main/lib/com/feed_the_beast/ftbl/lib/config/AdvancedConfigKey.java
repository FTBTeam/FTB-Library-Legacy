package com.feed_the_beast.ftbl.lib.config;

/**
 * @author LatvianModder
 */
public class AdvancedConfigKey extends ConfigKey
{
	public static final int EXCLUDED = 1;
	public static final int HIDDEN = 2;
	public static final int CANT_EDIT = 4;
	public static final int USE_SCROLL_BAR = 8;

	private final ConfigValue defValue;
	private boolean isExcluded, isHidden, cantEdit, useScrollBar;
	private String displayNameLangKey = "", group = "";

	public AdvancedConfigKey(String id, ConfigValue def, String group)
	{
		super(group.isEmpty() ? id : (group + "." + id));
		defValue = def;
		setGroup(group);
		setNameLangKey(getName());
	}

	public AdvancedConfigKey(String id, ConfigValue def)
	{
		this(id, def, "");
	}

	@Override
	public boolean isExcluded()
	{
		return isExcluded;
	}

	public void setExcluded(boolean v)
	{
		isExcluded = v;
	}

	@Override
	public boolean isHidden()
	{
		return isHidden;
	}

	public void setHidden(boolean v)
	{
		isHidden = v;
	}

	@Override
	public boolean cantEdit()
	{
		return cantEdit;
	}

	public void setCanEdit(boolean v)
	{
		cantEdit = !v;
	}

	@Override
	public boolean useScrollBar()
	{
		return useScrollBar;
	}

	public void setUseScrollBar(boolean v)
	{
		useScrollBar = v;
	}

	@Override
	public ConfigValue getDefValue()
	{
		return defValue;
	}

	@Override
	public String getNameLangKey()
	{
		return displayNameLangKey;
	}

	@Override
	public AdvancedConfigKey setNameLangKey(String key)
	{
		displayNameLangKey = key;
		return this;
	}

	@Override
	public String getGroup()
	{
		return group;
	}

	@Override
	public AdvancedConfigKey setGroup(String g)
	{
		group = g;
		return this;
	}
}