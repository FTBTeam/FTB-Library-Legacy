package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.lib.util.misc.Color4I;

/**
 * @author LatvianModder
 */
public abstract class CentredTextButton extends Button
{
	public CentredTextButton(int x, int y, int w, int h, String txt)
	{
		super(x, y, w, h, txt);
		setIcon(DEFAULT_BACKGROUND);
	}

	@Override
	public Color4I renderTitleInCenter(GuiBase gui)
	{
		return gui.getContentColor();
	}

	@Override
	public void renderWidget(GuiBase gui)
	{
		super.renderWidget(gui);

		if (gui.isMouseOver(this))
		{
			DEFAULT_MOUSE_OVER.draw(this, Color4I.NONE);
		}
	}
}