package com.feed_the_beast.ftblib.lib.icon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author LatvianModder
 */
public class IconWithTint extends Icon
{
	public final Icon parent;
	public final Color4I tint;

	IconWithTint(Icon p, Color4I c)
	{
		parent = p;
		tint = c;
	}

	@Override
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		parent.draw(x, y, w, h, (col.isEmpty() ? Color4I.WHITE : col).withTint(tint));
	}

	@Override
	public JsonElement getJson()
	{
		if (tint.isEmpty())
		{
			return parent.getJson();
		}

		JsonObject json = new JsonObject();
		json.addProperty("id", "tint");
		json.add("tint", tint.getJson());
		json.add("parent", parent.getJson());
		return json;
	}
}