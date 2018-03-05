package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.Icon;

/**
 * @author LatvianModder
 */
public abstract class Theme
{
	public abstract Color4I getContentColor(WidgetType type);

	public abstract Icon getGui(WidgetType type);

	public abstract Icon getWidget(WidgetType type);

	public abstract Icon getSlot(WidgetType type);

	public Icon getContainerSlot()
	{
		return getSlot(WidgetType.NORMAL).withBorder(-1);
	}

	public Icon getButton(WidgetType type)
	{
		return getWidget(type);
	}

	public Icon getScrollBarBackground()
	{
		return getSlot(WidgetType.NORMAL);
	}

	public Icon getScrollBar(WidgetType type, boolean vertical)
	{
		return getWidget(type);
	}

	public Icon getTextBox()
	{
		return getSlot(WidgetType.NORMAL);
	}

	public Icon getCheckboxBackground(boolean radioButton)
	{
		return getSlot(WidgetType.NORMAL);
	}

	public Icon getCheckbox(WidgetType type, boolean selected, boolean radioButton)
	{
		return selected ? getWidget(type) : Icon.EMPTY;
	}

	public Icon getPanelBackground()
	{
		return getContainerSlot();
	}
}