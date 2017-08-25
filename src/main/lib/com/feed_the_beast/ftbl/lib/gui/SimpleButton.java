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

	public SimpleButton(int x, int y, String text, IDrawableObject icon, BiConsumer<GuiBase, IMouseButton> c)
	{
		super(x, y, 16, 16, text);
		setIcon(icon);
		consumer = c;
	}

	public SimpleButton(int x, int y, LangKey text, IDrawableObject icon, BiConsumer<GuiBase, IMouseButton> c)
	{
		this(x, y, text.translate(), icon, c);
	}

	public SimpleButton(String text, IDrawableObject icon, BiConsumer<GuiBase, IMouseButton> c)
	{
		this(0, 0, text, icon, c);
	}

	public SimpleButton(LangKey text, IDrawableObject icon, BiConsumer<GuiBase, IMouseButton> c)
	{
		this(0, 0, text, icon, c);
	}

	@Override
	public void onClicked(GuiBase gui, IMouseButton button)
	{
		GuiHelper.playClickSound();
		consumer.accept(gui, button);
	}
}
