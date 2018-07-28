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
		setWidth(getStringWidth(getTitle()) + (getIcon().isEmpty() ? 8 : 28));
		return this;
	}

	public boolean renderTitleInCenter()
	{
		return false;
	}

	@Override
	public void addMouseOverText(List<String> list)
	{
		if (getStringWidth(getTitle()) + (getIcon().isEmpty() ? 8 : 28) > width)
		{
			list.add(getTitle());
		}
	}

	@Override
	public void draw()
	{
		int ax = getAX();
		int ay = getAY();

		getButtonBackground().draw(ax, ay, width, height);
		int off = (height - 16) / 2;
		Icon icon = getIcon();
		String title = getTitle();
		int textX = ax;
		int textY = ay + (height - getFontHeight() + 1) / 2;

		if (renderTitleInCenter())
		{
			textX += (width - getStringWidth(title) - (icon.isEmpty() ? 0 : off + 16)) / 2;
		}
		else
		{
			textX += 4;
		}

		if (!icon.isEmpty())
		{
			icon.draw(ax + off, ay + off, 16, 16);
			textX += off + 16;
		}

		drawString(title, textX, textY, getTheme().getContentColor(getWidgetType()), SHADOW);
	}
}