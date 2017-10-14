package com.feed_the_beast.ftbl.lib.icon;

import com.google.gson.JsonObject;
import net.minecraft.client.renderer.GlStateManager;

/**
 * @author LatvianModder
 */
public class ColoredIcon extends Icon
{
	public final Icon parent;
	public final Color4I color;
	public int border;

	ColoredIcon(Icon p, Color4I c, int b)
	{
		parent = p;
		color = c;
		border = b;
	}

	@Override
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		x += border;
		y += border;
		w -= border * 2;
		h -= border * 2;

		Color4I col1 = col.isEmpty() ? color : col;

		if (parent.isEmpty())
		{
			col1.draw(x, y, w, h);
		}
		else
		{
			parent.draw(x, y, w, h, col1);
		}

		GlStateManager.color(1F, 1F, 1F, 1F);
	}

	@Override
	public JsonObject getJson()
	{
		JsonObject o = new JsonObject();
		o.addProperty("id", "colored");

		if (!color.isEmpty())
		{
			o.add("color", color.getJson());
		}

		if (border != 0)
		{
			o.addProperty("border", border);
		}

		o.add("parent", parent.getJson());
		return o;
	}
}