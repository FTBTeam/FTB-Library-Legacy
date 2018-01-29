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

	public SimpleButton(GuiBase gui, String text, Icon icon, Callback c)
	{
		super(gui, text, icon);
		consumer = c;
	}

	public SimpleButton(GuiBase gui, LangKey text, Icon icon, Callback c)
	{
		this(gui, text.translate(), icon, c);
	}

	@Override
	public void onClicked(MouseButton button)
	{
		GuiHelper.playClickSound();
		consumer.onClicked(gui, button);
	}
}