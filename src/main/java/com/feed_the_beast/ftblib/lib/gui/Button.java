package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;

public abstract class Button extends Widget
{
	private String title = "";
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
		if (icon.isEmpty())
		{
			return getButtonBackground();
		}

		return icon;
	}

	@Override
	public boolean mousePressed(MouseButton button)
	{
		if (isMouseOver() && getWidgetType() != WidgetType.DISABLED)
		{
			onClicked(button);
			return true;
		}

		return false;
	}

	public abstract void onClicked(MouseButton button);
}