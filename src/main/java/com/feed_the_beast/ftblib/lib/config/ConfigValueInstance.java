package com.feed_the_beast.ftblib.lib.config;

/**
 * @author LatvianModder
 */
public final class ConfigValueInstance
{
	public final ConfigValueInfo info;
	public final ConfigValue value;

	public ConfigValueInstance(ConfigValueInfo i, ConfigValue v)
	{
		info = i;
		value = v;
	}
}