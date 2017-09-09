package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.lib.LangKey;
import com.feed_the_beast.ftbl.lib.MouseButton;
import com.feed_the_beast.ftbl.lib.icon.Icon;

import java.util.function.BiConsumer;

/**
 * @author LatvianModder
 */
public class SimpleButton extends Button
{
	private final BiConsumer<GuiBase, MouseButton> consumer;

	public SimpleButton(int x, int y, String text, Icon icon, BiConsumer<GuiBase, MouseButton> c)
	{
		super(x, y, 16, 16, text);
		setIcon(icon);
		consumer = c;
	}

	public SimpleButton(int x, int y, LangKey text, Icon icon, BiConsumer<GuiBase, MouseButton> c)
	{
		this(x, y, text.translate(), icon, c);
	}

	public SimpleButton(String text, Icon icon, BiConsumer<GuiBase, MouseButton> c)
	{
		this(0, 0, text, icon, c);
	}

	public SimpleButton(LangKey text, Icon icon, BiConsumer<GuiBase, MouseButton> c)
	{
		this(0, 0, text, icon, c);
	}

	@Override
	public void onClicked(GuiBase gui, MouseButton button)
	{
		GuiHelper.playClickSound();
		consumer.accept(gui, button);
	}
}
