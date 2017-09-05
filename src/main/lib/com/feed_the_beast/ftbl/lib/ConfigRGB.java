package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.lib.util.ColorUtils;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

/**
 * @author LatvianModder
 */
public final class ConfigRGB
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

	private Color4I color = Color4I.WHITE;

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

	public ConfigCategory from(Configuration config, String name, String category, int defR, int defG, int defB)
	{
		String cat = category + '.' + name;
		red = config.getInt("red", cat, defR, 0, 255, "");
		green = config.getInt("green", cat, defR, 0, 255, "");
		blue = config.getInt("blue", cat, defR, 0, 255, "");
		updateColor();
		return config.getCategory(cat);
	}

	public ConfigCategory from(Configuration config, String name, String category, int defCol)
	{
		return from(config, name, category, ColorUtils.getRed(defCol), ColorUtils.getGreen(defCol), ColorUtils.getBlue(defCol));
	}

	public void updateColor()
	{
		color = Color4I.rgb(red, green, blue);
	}

	public Color4I getColor()
	{
		return color;
	}
}