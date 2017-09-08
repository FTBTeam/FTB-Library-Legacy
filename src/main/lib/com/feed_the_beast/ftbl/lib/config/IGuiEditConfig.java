package com.feed_the_beast.ftbl.lib.config;

import com.google.gson.JsonElement;

/**
 * @author LatvianModder
 */
public interface IGuiEditConfig
{
	void onChanged(ConfigKey key, JsonElement json);

	void openGui();
}