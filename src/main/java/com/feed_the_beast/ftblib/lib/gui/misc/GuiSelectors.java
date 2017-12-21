package com.feed_the_beast.ftblib.lib.gui.misc;

import com.feed_the_beast.ftblib.lib.config.ConfigColor;
import com.feed_the_beast.ftblib.lib.config.ConfigValue;

/**
 * @author LatvianModder
 */
public class GuiSelectors
{
	public static void selectJson(ConfigValue value, IGuiFieldCallback callback)
	{
		new GuiConfigValueField(value, callback).openGui();
	}

	public static void selectColor(ConfigColor value, IGuiFieldCallback callback)
	{
		new GuiColorField(value, callback).openGui();
	}
}