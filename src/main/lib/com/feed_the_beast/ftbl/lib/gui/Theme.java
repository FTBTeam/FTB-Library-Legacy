package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.lib.icon.Icon;

/**
 * @author LatvianModder
 */
public abstract class Theme
{
	public enum WidgetType
	{
		NORMAL,
		MOUSE_OVER,
		DARK
	}

	public abstract Icon getGui(int w, int h, WidgetType type);

	public abstract Icon getWidged(int w, int h, WidgetType type);

	public abstract Icon getSlot(int w, int h, WidgetType type);
}