package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.lib.icon.Color4I;
import com.feed_the_beast.ftbl.lib.icon.ColoredIcon;
import com.feed_the_beast.ftbl.lib.icon.Icon;

/**
 * @author LatvianModder
 */
public abstract class Theme
{
	public static Theme CURRENT;

	public abstract Color4I getContentColor();

	public abstract Icon getGui(boolean mouseOver);

	public abstract Icon getWidget(boolean mouseOver);

	public Icon getDisabledWidget()
	{
		return getWidget(false);
	}

	public abstract Icon getSlot(boolean mouseOver);

	public Icon getButton(boolean mouseOver)
	{
		return getWidget(mouseOver);
	}

	public Icon getDisabledButton()
	{
		return getButton(false);
	}

	public Icon getScrollBarBackground()
	{
		return getSlot(false);
	}

	public Icon getScrollBar(boolean grabbed, boolean vertical)
	{
		return getWidget(grabbed);
	}

	public Icon getTextBox()
	{
		return getSlot(false);
	}

	public Icon getCheckboxBackground(boolean radioButton)
	{
		return getSlot(false);
	}

	public Icon getCheckbox(boolean mouseOver, boolean selected, boolean radioButton)
	{
		return selected ? getWidget(mouseOver) : Icon.EMPTY;
	}

	public Icon getPanelBackground()
	{
		return new ColoredIcon(getSlot(false), Icon.EMPTY, -1);
	}
}