package com.feed_the_beast.ftbl.lib.config;

/**
 * @author LatvianModder
 */
public class RankConfigKey extends AdvancedConfigKey
{
	private final ConfigValue defaultOPValue;

	public RankConfigKey(String s, ConfigValue def, ConfigValue defOP)
	{
		super(s, def);
		defaultOPValue = def.copy();
		defaultOPValue.fromJson(defOP.getSerializableElement());
	}

	public ConfigValue getDefOPValue()
	{
		return defaultOPValue;
	}
}