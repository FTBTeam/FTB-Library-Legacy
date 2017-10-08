package com.feed_the_beast.ftbl.lib.icon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public class CombinedIcon extends Icon
{
	public final List<Icon> list;

	public CombinedIcon(Collection<Icon> icons)
	{
		list = new ArrayList<>(icons);
	}

	public CombinedIcon(Icon... icons)
	{
		list = new ArrayList<>(icons.length);
		list.addAll(Arrays.asList(icons));
	}

	public CombinedIcon(Icon o1, Icon o2)
	{
		list = new ArrayList<>();
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