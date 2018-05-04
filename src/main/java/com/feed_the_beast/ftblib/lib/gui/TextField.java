package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.util.StringUtils;

import java.util.List;

/**
 * @author LatvianModder
 */
public class TextField extends Widget
{
	public String[] text = StringUtils.EMPTY_ARRAY;
	public int textFlags = 0;
	public boolean autoSizeWidth = true, autoSizeHeight = true;

	public TextField(Panel panel, String txt, int flags)
	{
		super(panel);
		textFlags = flags;
		setText(txt);
	}

	public TextField(Panel panel, String txt)
	{
		this(panel, txt, 0);
	}

	public TextField setText(String txt)
	{
		text = null;

		if (!txt.isEmpty())
		{
			text = autoSizeWidth ? txt.split("\n") : listFormattedStringToWidth(txt, width).toArray(StringUtils.EMPTY_ARRAY);
		}

		if (text == null || text.length == 0)
		{
			text = StringUtils.EMPTY_ARRAY;
		}

		if (autoSizeWidth)
		{
			setWidth(0);

			for (String s : text)
			{
				setWidth(Math.max(width, getStringWidth(s)));
			}
		}

		if (autoSizeHeight)
		{
			setHeight(Math.max(text.length, 1) * (getFontHeight() + 1));
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

		if (text.length == 0)
		{
			return;
		}

		for (int i = 0; i < text.length; i++)
		{
			drawString(text[i], ax, ay + i * 10 + 1, textFlags);
		}
	}
}
