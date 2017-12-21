package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.LangKey;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;

/**
 * @author LatvianModder
 */
public class SimpleButton extends Button
{
	public interface Callback
	{
		void onClicked(GuiBase gui, MouseButton button);
	}

	private final Callback consumer;

	public SimpleButton(GuiBase gui, int x, int y, String text, Icon icon, Callback c)
	{
		super(gui, x, y, 16, 16, text, icon);
		consumer = c;
	}

	public SimpleButton(GuiBase gui, int x, int y, LangKey text, Icon icon, Callback c)
	{
		this(gui, x, y, text.translate(), icon, c);
	}

	public SimpleButton(GuiBase gui, String text, Icon icon, Callback c)
	{
		this(gui, 0, 0, text, icon, c);
	}

	public SimpleButton(GuiBase gui, LangKey text, Icon icon, Callback c)
	{
		this(gui, 0, 0, text, icon, c);
	}

	@Override
	public void onClicked(MouseButton button)
	{
		GuiHelper.playClickSound();
		consumer.onClicked(gui, button);
	}
}