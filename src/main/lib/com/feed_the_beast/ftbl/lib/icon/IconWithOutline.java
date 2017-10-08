package com.feed_the_beast.ftbl.lib.icon;

import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.GlStateManager;

/**
 * @author LatvianModder
 */
public class IconWithOutline extends Icon
{
	public static final Icon BUTTON_GRAY = getIconWithOutline(Color4I.rgb(0x212121), Color4I.rgb(0x141414), false);
	public static final Icon BUTTON_RED = getIconWithOutline(Color4I.rgb(0x1581B6), Color4I.rgb(0xBF3726), false);
	public static final Icon BUTTON_GREEN = getIconWithOutline(Color4I.rgb(0x98C600), Color4I.rgb(0x438700), false);
	public static final Icon BUTTON_BLUE = getIconWithOutline(Color4I.rgb(0x80C7F2), Color4I.rgb(0x1581B6), false);

	public static final Icon BUTTON_ROUND_GRAY = getIconWithOutline(Color4I.rgb(0x212121), Color4I.rgb(0x141414), true);
	public static final Icon BUTTON_ROUND_RED = getIconWithOutline(Color4I.rgb(0x1581B6), Color4I.rgb(0xBF3726), true);
	public static final Icon BUTTON_ROUND_GREEN = getIconWithOutline(Color4I.rgb(0x98C600), Color4I.rgb(0x438700), true);
	public static final Icon BUTTON_ROUND_BLUE = getIconWithOutline(Color4I.rgb(0x80C7F2), Color4I.rgb(0x1581B6), true);

	public final Icon icon;
	public final Color4I color;
	public final boolean roundEdges;

	public static Icon getIconWithOutline(Icon icon, Color4I color, boolean roundEdges)
	{
		if (color.isEmpty())
		{
			return icon;
		}

		return new IconWithOutline(icon, color, roundEdges);
	}

	private IconWithOutline(Icon i, Color4I c, boolean r)
	{
		icon = i;
		color = c;
		roundEdges = r;
	}

	@Override
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		icon.draw(x + 1, y + 1, w - 2, h - 2, col);
		GuiHelper.drawHollowRect(x, y, w, h, color, roundEdges);
		GlStateManager.color(1F, 1F, 1F, 1F);
	}

	@Override
	public JsonElement getJson()
	{
		JsonObject o = new JsonObject();
		o.addProperty("id", "outline");
		o.add("icon", icon.getJson());
		o.add("color", color.getJson());
		if (roundEdges)
		{
			o.addProperty("round_edges", true);
		}
		return o;
	}
}