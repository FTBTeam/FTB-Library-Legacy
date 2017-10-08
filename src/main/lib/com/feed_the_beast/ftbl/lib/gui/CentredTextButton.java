package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.lib.icon.Color4I;

/**
 * @author LatvianModder
 */
public abstract class CentredTextButton extends Button
{
	public CentredTextButton(GuiBase gui, int x, int y, int w, int h, String txt)
	{
		super(gui, x, y, w, h, txt);
	}

	@Override
	public Color4I renderTitleInCenter()
	{
		return gui.getTheme().getContentColor(false);
	}
}