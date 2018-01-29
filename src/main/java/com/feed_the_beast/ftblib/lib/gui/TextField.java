package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.util.CommonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class TextField extends Widget
{
	public List<String> text = Collections.emptyList();
	public int textFlags = 0;
	public boolean autoSizeWidth, autoSizeHeight;

	public TextField(GuiBase gui, String txt, int flags)
	{
		super(gui);
		textFlags = flags;
		setText(txt);
	}

	public TextField(GuiBase gui, String txt)
	{
		this(gui, txt, 0);
	}

	public TextField setText(String txt)
	{
		text = null;

		if (!txt.isEmpty())
		{
			text = new ArrayList<>(autoSizeWidth ? CommonUtils.asList(txt.split("\n")) : gui.listFormattedStringToWidth(txt, width));
		}

		if (text == null || text.isEmpty())
		{
			text = Collections.emptyList();
		}

		if (autoSizeWidth)
		{
			setWidth(0);

			for (String s : text)
			{
				setWidth(Math.max(width, gui.getStringWidth(s)));
			}
		}

		if (autoSizeHeight)
		{
			int h1 = gui.getFontHeight() + 1;
			setHeight(text.isEmpty() ? h1 : h1 * text.size());
		}

		return this;
	}

	@Override
	public void addMouseOverText(List<String> list)
	{
	}

	@Override
	public void draw()
	{
		int ay = getAY();
		int ax = getAX();

		getIcon().draw(ax, ay, width, height);

		if (text.isEmpty())
		{
			return;
		}

		for (int i = 0; i < text.size(); i++)
		{
			gui.drawString(text.get(i), ax, ay + i * 10 + 1, textFlags);
		}
	}
}
