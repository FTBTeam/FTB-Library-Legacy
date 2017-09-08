package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.lib.config.ConfigValue;

/**
 * @author LatvianModder
 */
public interface IGuiFieldCallback
{
	void onCallback(ConfigValue value, boolean set);
}