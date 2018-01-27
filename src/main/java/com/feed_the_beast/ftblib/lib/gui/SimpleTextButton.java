package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.icon.Icon;

import java.util.List;

/**
 * @author LatvianModder
 */
public abstract class SimpleTextButton extends Button
{
	public SimpleTextButton(GuiBase gui, int x, int y, String txt, Icon icon)
	{
		super(gui, x, y, 0, 20, txt, icon);
	}

	@Override
	public SimpleTextButton setTitle(String txt)
	{
		super.setTitle(txt);
		setWidth(gui.getStringWidth(getTitle()) + (getIcon().isEmpty() ? 8 : 28));
		return this;
	}

	public boolean renderTitleInCenter()
	{
		return false;
	}

	@Override
	public void addMouseOverText(List<String> list)
	{
	}

	@Override
	public Icon getIcon()
	{
		return icon;
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
		int textY = ay + (height - gui.getFontHeight() + 1) / 2;

		if (renderTitleInCenter())
		{
			textX += (width - gui.getStringWidth(title) - (icon.isEmpty() ? 0 : off + 16)) / 2;
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

		gui.drawString(title, textX, textY, SHADOW);
	}
}