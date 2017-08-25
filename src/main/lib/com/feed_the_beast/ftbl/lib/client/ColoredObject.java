package com.feed_the_beast.ftbl.lib.client;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.GlStateManager;

/**
 * @author LatvianModder
 */
public class ColoredObject implements IDrawableObject
{
	public final IDrawableObject parent;
	public final Color4I color;

	public ColoredObject(IDrawableObject p, Color4I c)
	{
		parent = p;
		color = c;
	}

	public ColoredObject(IDrawableObject p, int c)
	{
		this(p, Color4I.rgba(c).mutable());
	}

	@Override
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		Color4I col1 = col.hasColor() ? col : color;

		if (parent.isNull())
		{
			GuiHelper.drawBlankRect(x, y, w, h, col1);
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

		if (color.hasColor())
		{
			o.add("color", color.toJson());
		}

		o.add("parent", parent.getJson());
		return o;
	}
}