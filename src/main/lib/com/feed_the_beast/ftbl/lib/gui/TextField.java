package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.lib.util.LangKey;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;

import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class TextField extends Widget
{
	public List<String> text = Collections.emptyList();

	public TextField(GuiBase gui, int x, int y, int w, int h, String txt)
	{
		super(gui, x, y, w, h);
		setTitle(txt);
	}

	public TextField(GuiBase gui, int x, int y, int w, int h, LangKey key)
	{
		this(gui, x, y, w, h, key.translate());
	}

	public TextField(GuiBase gui, int x, int y, int w, int h, ITextComponent component)
	{
		this(gui, x, y, w, h, component.getFormattedText());
	}

	public TextField setTitle(String txt)
	{
		text.clear();

		if (!txt.isEmpty())
		{
			text = gui.listFormattedStringToWidth(txt, width);
		}

		if (text.isEmpty())
		{
			text = Collections.emptyList();
		}

		if (height <= 1)
		{
			int h1 = gui.getFontHeight() + 1;
			setHeight(text.isEmpty() ? h1 : h1 * text.size());
		}

		if (width <= 1)
		{
			for (String s : text)
			{
				setWidth(Math.max(width, gui.getStringWidth(s)));
			}
		}

		return this;
	}

	@Override
	public void addMouseOverText(List<String> list)
	{
	}

	@Override
	public void renderWidget()
	{
		if (text.isEmpty())
		{
			return;
		}

		int ay = getAY();
		int ax = getAX();

		for (int i = 0; i < text.size(); i++)
		{
			gui.drawString(text.get(i), ax, ay + i * 10 + 1, DARK);
		}

		GlStateManager.color(1F, 1F, 1F, 1F);
	}
}
