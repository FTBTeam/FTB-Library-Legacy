package com.feed_the_beast.ftblib.lib.icon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author LatvianModder
 */
public class IconWithBorder extends IconWithParent
{
	public int border;

	IconWithBorder(Icon p, int b)
	{
		super(p);
		border = b;
	}

	@Override
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		x += border;
		y += border;
		w -= border * 2;
		h -= border * 2;
		parent.draw(x, y, w, h, col);
	}

	@Override
	public JsonElement getJson()
	{
		if (border == 0)
		{
			return parent.getJson();
		}

		JsonObject json = new JsonObject();
		json.addProperty("id", "border");
		json.addProperty("border", border);
		json.add("parent", parent.getJson());
		return json;
	}
}