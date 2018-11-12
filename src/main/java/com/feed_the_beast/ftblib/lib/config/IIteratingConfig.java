package com.feed_the_beast.ftblib.lib.config;

/**
 * @author LatvianModder
 */
public interface IIteratingConfig
{
	void iterate(ConfigValueInstance inst, boolean next);

	ConfigValue getIteration(boolean next);
}