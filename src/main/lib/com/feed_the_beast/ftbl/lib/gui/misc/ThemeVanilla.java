package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.lib.gui.Theme;
import com.feed_the_beast.ftbl.lib.icon.Icon;

/**
 * @author LatvianModder
 */
public class ThemeVanilla extends Theme
{
	public static final ThemeVanilla INSTANCE = new ThemeVanilla();

	private ThemeVanilla()
	{
	}

	@Override
	public Icon getGui(int w, int h, WidgetType type)
	{
		return null;
	}

	@Override
	public Icon getWidged(int w, int h, WidgetType type)
	{
		return null;
	}

	@Override
	public Icon getSlot(int w, int h, WidgetType type)
	{
		return null;
	}
}