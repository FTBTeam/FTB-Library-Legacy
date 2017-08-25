package com.feed_the_beast.ftbl.lib.client;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.math.MathUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public class IconAnimation implements IDrawableObject
{
	public final List<IDrawableObject> list;
	private IDrawableObject current = ImageProvider.NULL;
	public long timer = 1000L;

	public IconAnimation(Collection<IDrawableObject> l)
	{
		list = new ArrayList<>(l.size());

		for (IDrawableObject o : l)
		{
			if (o != null && !o.isNull())
			{
				list.add(o);
			}
		}

		if (!list.isEmpty())
		{
			current = list.get(0);
		}
	}

	public int getItemCount()
	{
		return list.size();
	}

	public IDrawableObject getObject(int index)
	{
		if (index < 0 || index >= list.size())
		{
			return current;
		}

		return list.get(index);
	}

	public void setIndex(int i)
	{
		current = list.get(MathUtils.wrap(i, list.size()));
	}

	@Override
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		if (current != null)
		{
			current.draw(x, y, w, h, col);
		}

		if (!list.isEmpty())
		{
			setIndex((int) (System.currentTimeMillis() / timer));
		}
	}

	@Override
	public JsonElement getJson()
	{
		JsonObject json = new JsonObject();
		json.addProperty("id", "animation");

		if (timer != 1000L)
		{
			json.addProperty("timer", timer);
		}

		JsonArray array = new JsonArray();

		for (IDrawableObject o : list)
		{
			array.add(o.getJson());
		}

		json.add("icons", array);
		return json;
	}
}