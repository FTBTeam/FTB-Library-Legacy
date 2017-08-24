package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.LangKey;

import java.util.function.BiConsumer;

/**
 * @author LatvianModder
 */
public class SimpleButton extends Button
{
	private final BiConsumer<GuiBase, IMouseButton> consumer;

	public SimpleButton(int x, int y, int w, int h, String text, IDrawableObject icon, BiConsumer<GuiBase, IMouseButton> c)
	{
		super(x, y, w, h, text);
		setIcon(icon);
		consumer = c;
	}

	public SimpleButton(int x, int y, int w, int h, LangKey text, IDrawableObject icon, BiConsumer<GuiBase, IMouseButton> c)
	{
		this(x, y, w, h, text.translate(), icon, c);
	}

	@Override
	public void onClicked(GuiBase gui, IMouseButton button)
	{
		GuiHelper.playClickSound();
		consumer.accept(gui, button);
	}
}
