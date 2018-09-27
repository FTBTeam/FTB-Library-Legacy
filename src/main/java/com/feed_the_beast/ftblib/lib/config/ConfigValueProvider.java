package com.feed_the_beast.ftblib.lib.config;

import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
@FunctionalInterface
public interface ConfigValueProvider extends Supplier<ConfigValue>
{
}