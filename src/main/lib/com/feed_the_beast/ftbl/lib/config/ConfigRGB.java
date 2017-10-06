package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.lib.util.ColorUtils;
import com.feed_the_beast.ftbl.lib.util.misc.Color4I;
import net.minecraftforge.common.config.Config;

/**
 * @author LatvianModder
 */
public class ConfigRGB
{
	@Config.RangeInt(min = 0, max = 255)
	@Config.LangKey("item.fireworksCharge.red")
	public int red;

	@Config.RangeInt(min = 0, max = 255)
	@Config.LangKey("item.fireworksCharge.green")
	public int green;

	@Config.RangeInt(min = 0, max = 255)
	@Config.LangKey("item.fireworksCharge.blue")
	public int blue;

	public ConfigRGB(int r, int g, int b)
	{
		red = r;
		green = g;
		blue = b;
	}

	public ConfigRGB(int color)
	{
		red = ColorUtils.getRed(color);
		green = ColorUtils.getGreen(color);
		blue = ColorUtils.getBlue(color);
	}

	public Color4I createColor(int a)
	{
		return Color4I.rgba(red, green, blue, a);
	}

	public Color4I createColor()
	{
		return createColor(255);
	}
}