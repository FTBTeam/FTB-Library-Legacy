package com.feed_the_beast.ftbl.lib.client;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public class CombinedIcon implements IDrawableObject
{
	public final List<IDrawableObject> list;

	public CombinedIcon(Collection<IDrawableObject> icons)
	{
		list = new ArrayList<>(icons);
	}

	public CombinedIcon(IDrawableObject... icons)
	{
		list = new ArrayList<>(icons.length);
		list.addAll(Arrays.asList(icons));
	}

	public CombinedIcon(IDrawableObject o1, IDrawableObject o2)
	{
		list = new ArrayList<>();
		list.add(o1);
		list.add(o2);
	}

	@Override
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		for (IDrawableObject icon : list)
		{
			icon.draw(x, y, w, h, col);
		}
	}

	@Override
	public JsonElement getJson()
	{
		JsonArray json = new JsonArray();

		for (IDrawableObject o : list)
		{
			json.add(o.getJson());
		}

		return json;
	}
}