package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonElement;

/**
 * @author LatvianModder
 */
public interface IGuiEditConfig
{
	void onChanged(IConfigKey key, JsonElement json);

	void openGui();
}