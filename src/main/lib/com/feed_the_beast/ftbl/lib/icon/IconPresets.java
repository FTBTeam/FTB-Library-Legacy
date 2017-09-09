package com.feed_the_beast.ftbl.lib.icon;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class IconPresets
{
	public static final Map<String, Icon> MAP = new HashMap<>();

	static
	{
		MAP.put("#gray_button", TexturelessRectangle.BUTTON_GRAY);
		MAP.put("#red_button", TexturelessRectangle.BUTTON_RED);
		MAP.put("#green_button", TexturelessRectangle.BUTTON_GREEN);
		MAP.put("#blue_button", TexturelessRectangle.BUTTON_BLUE);
		MAP.put("#gray_round_button", TexturelessRectangle.BUTTON_ROUND_GRAY);
		MAP.put("#red_round_button", TexturelessRectangle.BUTTON_ROUND_RED);
		MAP.put("#green_round_button", TexturelessRectangle.BUTTON_ROUND_GREEN);
		MAP.put("#blue_round_button", TexturelessRectangle.BUTTON_ROUND_BLUE);
	}
}