package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.icon.Icon;

import java.util.List;

/**
 * @author LatvianModder
 */
public abstract class SimpleTextButton extends Button
{
	public SimpleTextButton(Panel panel, String txt, Icon icon)
	{
		super(panel, txt, icon);
		setHeight(20);
	}

	@Override
	public SimpleTextButton setTitle(String txt)
	{
		super.setTitle(txt);
		setWidth(getGui().getTheme().getStringWidth(getTitle()) + (icon.isEmpty() ? 8 : 28));
		return this;
	}

	public boolean renderTitleInCenter()
	{
		return false;
	}

	@Override
	public void addMouseOverText(List<String> list)
	{
		if (getGui().getTheme().getStringWidth(getTitle()) + (icon.isEmpty() ? 8 : 28) > width)
		{
			list.add(getTitle());
		}
	}

	@Override
	public void draw(Theme theme, int x, int y, int w, int h)
	{
		drawBackground(theme, x, y, w, h);
		int s = h >= 16 ? 16 : 8;
		int off = (h - s) / 2;
		String title = getTitle();
		int textX = x;
		int textY = y + (h - theme.getFontHeight() + 1) / 2;

		if (renderTitleInCenter())
		{
			textX += (w - theme.getStringWidth(title) - (icon.isEmpty() ? 0 : off + s)) / 2;
		}
		else
		{
			textX += 4;
		}

		if (!icon.isEmpty())
		{
			icon.draw(x + off, y + off, s, s);
			textX += off + s;
		}

		theme.drawString(title, textX, textY, theme.getContentColor(getWidgetType()), Theme.SHADOW);
	}
}