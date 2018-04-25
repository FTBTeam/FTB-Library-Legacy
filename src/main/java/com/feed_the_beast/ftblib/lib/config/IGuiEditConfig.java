package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.util.misc.Node;
import com.google.gson.JsonElement;

/**
 * @author LatvianModder
 */
public interface IGuiEditConfig
{
	void onChanged(Node key, JsonElement json);

	void openGui();
}