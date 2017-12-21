package com.feed_the_beast.ftblib.lib.config;

/**
 * @author LatvianModder
 */
public class RankConfigValueInfo extends ConfigValueInfo
{
	public ConfigValue defaultOPValue;

	public RankConfigValueInfo(String s, ConfigValue def, ConfigValue defOP)
	{
		super(s);
		defaultValue = def.copy();
		defaultOPValue = def.copy();
		defaultOPValue.fromJson(defOP.getSerializableElement());
	}
}