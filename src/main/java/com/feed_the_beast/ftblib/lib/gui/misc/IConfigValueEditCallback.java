package com.feed_the_beast.ftblib.lib.gui.misc;

import com.feed_the_beast.ftblib.lib.config.ConfigValue;

/**
 * @author LatvianModder
 */
@FunctionalInterface
public interface IConfigValueEditCallback
{
	void onCallback(ConfigValue value, boolean set);
}