package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.api.config.IConfigValue;

/**
 * @author LatvianModder
 */
public interface IGuiFieldCallback
{
	void onCallback(IConfigValue value, boolean set);
}