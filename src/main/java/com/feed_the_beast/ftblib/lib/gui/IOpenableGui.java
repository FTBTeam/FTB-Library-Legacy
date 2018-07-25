package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;

/**
 * @author LatvianModder
 */
public interface IOpenableGui
{
	void openGui();

	default void openGuiLater()
	{
		ClientUtils.runLater(this::openGui);
	}

	default void closeGui()
	{
		closeGui(true);
	}

	default void closeGui(boolean openPrevScreen)
	{
	}
}