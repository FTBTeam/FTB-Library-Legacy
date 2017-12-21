package com.feed_the_beast.ftblib.lib.config;

import com.google.gson.JsonElement;

/**
 * @author LatvianModder
 */
public interface IGuiEditConfig
{
	void onChanged(String key, JsonElement json);

	void openGui();
}