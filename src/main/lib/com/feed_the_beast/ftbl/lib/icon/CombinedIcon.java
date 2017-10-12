package com.feed_the_beast.ftbl.lib.icon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public class CombinedIcon extends Icon
{
	public static Icon getCombined(Collection<Icon> icons)
	{
		List<Icon> list = new ArrayList<>(icons.size());

		for (Icon icon : icons)
		{
			if (!icon.isEmpty())
			{
				list.add(icon);
			}
		}

		if (list.isEmpty())
		{
			return EMPTY;
		}
		else if (list.size() == 1)
		{
			return list.get(0);
		}

		return new CombinedIcon(list);
	}

	public final List<Icon> list;

	CombinedIcon(Collection<Icon> icons)
	{
		list = new ArrayList<>(icons.size());

		for (Icon icon : icons)
		{
			if (!icon.isEmpty())
			{
				list.add(icon);
			}
		}
	}

	CombinedIcon(Icon o1, Icon o2)
	{
		list = new ArrayList<>(2);
		list.add(o1);
		list.add(o2);
	}

	@Override
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		for (Icon icon : list)
		{
			icon.draw(x, y, w, h, col);
		}
	}

	@Override
	public JsonElement getJson()
	{
		JsonArray json = new JsonArray();

		for (Icon o : list)
		{
			json.add(o.getJson());
		}

		return json;
	}
}