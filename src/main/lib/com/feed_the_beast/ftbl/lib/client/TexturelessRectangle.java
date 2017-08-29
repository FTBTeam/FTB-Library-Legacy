package com.feed_the_beast.ftbl.lib.client;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.GlStateManager;

/**
 * @author LatvianModder
 */
public class TexturelessRectangle implements IDrawableObject
{
	public static final TexturelessRectangle BUTTON_GRAY = new TexturelessRectangle(0xFF212121).setLineColor(0xFF141414);
	public static final TexturelessRectangle BUTTON_RED = new TexturelessRectangle(0xFF1581B6).setLineColor(0xFFBF3726);
	public static final TexturelessRectangle BUTTON_GREEN = new TexturelessRectangle(0xFF98C600).setLineColor(0xFF438700);
	public static final TexturelessRectangle BUTTON_BLUE = new TexturelessRectangle(0xFF80C7F2).setLineColor(0xFF1581B6);

	public static final TexturelessRectangle BUTTON_ROUND_GRAY = BUTTON_GRAY.copy().setRoundEdges(true);
	public static final TexturelessRectangle BUTTON_ROUND_RED = BUTTON_RED.copy().setRoundEdges(true);
	public static final TexturelessRectangle BUTTON_ROUND_GREEN = BUTTON_GREEN.copy().setRoundEdges(true);
	public static final TexturelessRectangle BUTTON_ROUND_BLUE = BUTTON_BLUE.copy().setRoundEdges(true);

	private Color4I color, lineColor;
	public boolean roundEdges = false;

	public TexturelessRectangle(Color4I col)
	{
		color = col;
		lineColor = Color4I.NONE;
	}

	public TexturelessRectangle(int col)
	{
		this(Color4I.rgba(col));
	}

	public Color4I getColor()
	{
		return color;
	}

	public Color4I getLineColor()
	{
		return lineColor;
	}

	public TexturelessRectangle setLineColor(Color4I col)
	{
		lineColor = col;
		return this;
	}

	public TexturelessRectangle setLineColor(int col)
	{
		if (lineColor.isMutable())
		{
			lineColor.mutable().set(col);
		}
		else
		{
			lineColor = Color4I.rgba(col).mutable();
		}

		return this;
	}

	public TexturelessRectangle setRoundEdges(boolean v)
	{
		roundEdges = v;
		return this;
	}

	public TexturelessRectangle copy()
	{
		TexturelessRectangle t = new TexturelessRectangle(color);
		t.setLineColor(lineColor);
		t.setRoundEdges(roundEdges);
		return t;
	}

	@Override
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		Color4I c = col.hasColor() ? col : color;

		if (roundEdges || lineColor.hasColor())
		{
			if (c.hasColor())
			{
				GuiHelper.drawBlankRect(x + 1, y + 1, w - 2, h - 2, c);
			}

			GuiHelper.drawHollowRect(x, y, w, h, lineColor.hasColor() ? lineColor : c, roundEdges);
			GlStateManager.color(1F, 1F, 1F, 1F);
		}
		else if (c.hasColor())
		{
			GuiHelper.drawBlankRect(x, y, w, h, c);
		}
	}

	@Override
	public JsonElement getJson()
	{
		JsonObject o = new JsonObject();
		o.addProperty("id", "rect");
		if (color.hasColor())
		{
			o.add("color", color.toJson());
		}
		if (lineColor.hasColor())
		{
			o.add("line_color", lineColor.toJson());
		}
		if (roundEdges)
		{
			o.addProperty("round_edges", true);
		}
		return o;
	}
}