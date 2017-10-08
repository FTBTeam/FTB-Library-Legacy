package com.feed_the_beast.ftbl.lib.icon;

import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
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
	private boolean inversed;

	public BulletIcon()
	{
		color = Icon.EMPTY;
		colorB = Icon.EMPTY;
		colorD = Icon.EMPTY;
	}

	public BulletIcon setColor(Color4I col)
	{
		color = col;

		if (color.isEmpty())
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

	public BulletIcon setInversed(boolean v)
	{
		inversed = v;
		return this;
	}

	@Override
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		Color4I c, cb, cd;

		if (!col.isEmpty())
		{
			c = col;
			MutableColor4I cm = col.mutable();
			cm.addBrightness(18);
			cb = cm.copy();
			cm = col.mutable();
			cm.addBrightness(-18);
			cd = cm.copy();
		}
		else if (color.isEmpty())
		{
			c = DEFAULT_COLOR;
			cb = DEFAULT_COLOR_B;
			cd = DEFAULT_COLOR_D;
		}
		else
		{
			c = color;
			cb = colorB;
			cd = colorD;
		}

		GlStateManager.disableTexture2D();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

		GuiHelper.addRectToBuffer(buffer, x, y + 1, 1, h - 2, inversed ? cd : cb);
		GuiHelper.addRectToBuffer(buffer, x + w - 1, y + 1, 1, h - 2, inversed ? cb : cd);
		GuiHelper.addRectToBuffer(buffer, x + 1, y, w - 2, 1, inversed ? cd : cb);
		GuiHelper.addRectToBuffer(buffer, x + 1, y + h - 1, w - 2, 1, inversed ? cb : cd);
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
		if (!color.isEmpty())
		{
			o.add("color", color.getJson());
		}
		return o;
	}
}