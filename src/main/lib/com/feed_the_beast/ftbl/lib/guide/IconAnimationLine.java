package com.feed_the_beast.ftbl.lib.guide;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.guide.IGuideTextLine;
import com.feed_the_beast.ftbl.lib.client.IconAnimation;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import com.feed_the_beast.ftbl.lib.gui.DrawableObjectListButton;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.math.MathHelper;

import java.util.Collections;

/**
 * @author LatvianModder
 */
public class IconAnimationLine extends EmptyGuidePageLine
{
	private final IconAnimation list;
	private final int cols;

	public IconAnimationLine(IconAnimation l, int columns)
	{
		list = l;
		cols = MathHelper.clamp(columns, 0, 16);
	}

	public IconAnimationLine(JsonElement json)
	{
		list = new IconAnimation(Collections.emptyList());

		if (json.isJsonObject())
		{
			JsonObject o = json.getAsJsonObject();
			cols = MathHelper.clamp(o.has("columns") ? o.get("columns").getAsInt() : 8, 0, 16);

			if (o.has("objects"))
			{
				for (JsonElement e : o.get("objects").getAsJsonArray())
				{
					list.list.add(ImageProvider.get(e));
				}
			}
		}
		else
		{
			cols = 8;

			for (JsonElement e : json.getAsJsonArray())
			{
				list.list.add(ImageProvider.get(e));
			}
		}
	}

	@Override
	public JsonElement getJson()
	{
		JsonObject o = new JsonObject();
		o.addProperty("id", "icon_animation");
		o.addProperty("columns", cols);
		JsonArray a = new JsonArray();

		for (IDrawableObject item : list.list)
		{
			a.add(item.getJson());
		}

		o.add("objects", a);
		return o;
	}

	@Override
	public Widget createWidget(GuiBase gui, Panel parent)
	{
		return new DrawableObjectListButton(0, 0, list, cols);
	}

	@Override
	public IGuideTextLine copy(GuidePage page)
	{
		return new IconAnimationLine(list, cols);
	}

	@Override
	public boolean isEmpty()
	{
		return list.list.isEmpty();
	}
}