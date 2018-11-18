package com.feed_the_beast.ftblib.lib.config;

/**
 * @author LatvianModder
 */
@FunctionalInterface
public interface IIteratingConfig
{
	ConfigValue getIteration(boolean next);
}