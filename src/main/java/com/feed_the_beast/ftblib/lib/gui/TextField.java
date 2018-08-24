package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.io.Bits;
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
		Theme theme = getGui().getTheme();
		text = null;

		if (!txt.isEmpty())
		{
			text = autoSizeWidth ? txt.split("\n") : theme.listFormattedStringToWidth(txt, width).toArray(StringUtils.EMPTY_ARRAY);
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
				setWidth(Math.max(width, theme.getStringWidth(s)));
			}
		}

		if (autoSizeHeight)
		{
			setHeight(Math.max(text.length, 1) * (theme.getFontHeight() + 1));
		}

		return this;
	}

	@Override
	public void addMouseOverText(List<String> list)
	{
	}

	public void drawTextField(Theme theme, int x, int y, int w, int h)
	{
	}

	@Override
	public void draw(Theme theme, int x, int y, int w, int h)
	{
		drawTextField(theme, x, y, w, h);

		if (text.length == 0)
		{
			return;
		}

		boolean centered = Bits.getFlag(textFlags, Theme.CENTERED);

		for (int i = 0; i < text.length; i++)
		{
			if (centered)
			{
				theme.drawString(text[i], x + w / 2, y + i * 10 + 1, textFlags);
			}
			else
			{
				theme.drawString(text[i], x, y + i * 10 + 1, textFlags);
			}
		}
	}
}
