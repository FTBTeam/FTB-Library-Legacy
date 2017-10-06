package com.feed_the_beast.ftbl.lib.icon;

import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.util.misc.Color4I;
import com.feed_the_beast.ftbl.lib.util.misc.MutableColor4I;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

/**
 * @author LatvianModder
 */
public class BulletIcon extends Icon
{
	private static final MutableColor4I DEFAULT_COLOR = Color4I.rgb(0xEDEDED).mutable();
	private static final MutableColor4I DEFAULT_COLOR_B = Color4I.rgb(0xFFFFFF).mutable();
	private static final MutableColor4I DEFAULT_COLOR_D = Color4I.rgb(0xDDDDDD).mutable();

	private Color4I color, colorB, colorD;

	public BulletIcon()
	{
		color = Color4I.NONE;
		colorB = Color4I.NONE;
		colorD = Color4I.NONE;
	}

	public BulletIcon setColor(Color4I col)
	{
		color = col;

		if (!color.hasColor())
		{
			return this;
		}

		MutableColor4I c = color.mutable();
		c.addBrightness(18);
		colorB = c.copy();
		c = color.mutable();
		c.addBrightness(-18);
		colorD = c.copy();
		return this;
	}

	@Override
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		Color4I c, cb, cd;

		if (col.hasColor())
		{
			c = col;
			MutableColor4I cm = col.mutable();
			cm.addBrightness(18);
			cb = cm.copy();
			cm = col.mutable();
			cm.addBrightness(-18);
			cd = cm.copy();
		}
		else if (color.hasColor())
		{
			c = color;
			cb = colorB;
			cd = colorD;
		}
		else
		{
			c = DEFAULT_COLOR;
			cb = DEFAULT_COLOR_B;
			cd = DEFAULT_COLOR_D;
		}

		GlStateManager.disableTexture2D();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

		GuiHelper.addRectToBuffer(buffer, x, y + 1, 1, h - 2, cb);
		GuiHelper.addRectToBuffer(buffer, x + w - 1, y + 1, 1, h - 2, cd);

		GuiHelper.addRectToBuffer(buffer, x + 1, y, w - 2, 1, cb);
		GuiHelper.addRectToBuffer(buffer, x + 1, y + h - 1, w - 2, 1, cd);

		GuiHelper.addRectToBuffer(buffer, x + 1, y + 1, w - 2, h - 2, c);

		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.color(1F, 1F, 1F, 1F);
	}

	@Override
	public JsonElement getJson()
	{
		JsonObject o = new JsonObject();
		o.addProperty("id", "bullet");
		if (color.hasColor())
		{
			o.add("color", color.toJson());
		}
		return o;
	}
}