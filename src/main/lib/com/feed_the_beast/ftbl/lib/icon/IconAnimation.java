package com.feed_the_beast.ftbl.lib.icon;

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
public class IconAnimation extends Icon
{
	public final List<Icon> list;
	private Icon current = Icon.EMPTY;
	public long timer = 1000L;

	public IconAnimation(Collection<Icon> l)
	{
		list = new ArrayList<>(l.size());

		for (Icon o : l)
		{
			if (o != null && !o.isEmpty())
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

	public Icon getObject(int index)
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

		for (Icon o : list)
		{
			array.add(o.getJson());
		}

		json.add("icons", array);
		return json;
	}
}