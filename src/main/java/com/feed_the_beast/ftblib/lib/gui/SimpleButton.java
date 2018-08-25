package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;

/**
 * @author LatvianModder
 */
public class SimpleButton extends Button
{
	public interface Callback
	{
		void onClicked(SimpleButton widget, MouseButton button);
	}

	private final Callback consumer;

	public SimpleButton(Panel panel, String text, Icon icon, Callback c)
	{
		super(panel, text, icon);
		consumer = c;
	}

	@Override
	public void drawBackground(Theme theme, int x, int y, int w, int h)
	{
	}

	@Override
	public void onClicked(MouseButton button)
	{
		GuiHelper.playClickSound();
		consumer.onClicked(this, button);
	}
}