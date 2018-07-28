package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;

public abstract class Button extends Widget
{
	protected String title = "";
	protected Icon icon = Icon.EMPTY;

	public Button(Panel panel)
	{
		super(panel);
		setSize(16, 16);
	}

	public Button(Panel panel, String title, Icon icon)
	{
		this(panel);
		setIcon(icon);
		setTitle(title);
	}

	@Override
	public String getTitle()
	{
		return title;
	}

	public Button setTitle(String s)
	{
		title = s;
		return this;
	}

	public Button setIcon(Icon i)
	{
		icon = i;
		return this;
	}

	public Icon getButtonBackground()
	{
		return getTheme().getButton(getWidgetType());
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
		getIcon().draw(ax + (width - 16) / 2, ay + (height - 16) / 2, 16, 16);
	}

	@Override
	public boolean mousePressed(MouseButton button)
	{
		if (isMouseOver())
		{
			if (getWidgetType() != WidgetType.DISABLED)
			{
				onClicked(button);
			}

			return true;
		}

		return false;
	}

	public abstract void onClicked(MouseButton button);
}