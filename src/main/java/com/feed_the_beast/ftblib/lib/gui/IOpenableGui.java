package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;

import javax.annotation.Nullable;

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

	default void openContextMenu(@Nullable Panel panel)
	{
		if (this instanceof Widget)
		{
			((Widget) this).getGui().openContextMenu(panel);
		}
	}

	default void closeContextMenu()
	{
		if (this instanceof Widget)
		{
			((Widget) this).getGui().closeContextMenu();
		}
		else
		{
			openContextMenu(null);
		}
	}
}