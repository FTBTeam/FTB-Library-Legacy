package com.feed_the_beast.ftbl.lib.icon;

import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.util.ColorUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class Color4I extends Icon
{
	public static final Object EMPTY = new Object();

	public static Color4I fromJson(@Nullable JsonElement element)
	{
		if (element == null || element.isJsonNull())
		{
			return Icon.EMPTY;
		}
		else if (element.isJsonPrimitive())
		{
			String s = element.getAsString();

			if (s.equals("-"))
			{
				return Icon.EMPTY;
			}
			else
			{
				String hex = s.substring(1);
				return hex.length() == 8 ? rgba((int) Long.parseLong(hex, 16)) : rgb((int) Long.parseLong(hex, 16));
			}
		}
		else if (element.isJsonArray())
		{
			JsonArray array = element.getAsJsonArray();

			if (array.size() >= 3)
			{
				int r = array.get(0).getAsInt();
				int g = array.get(1).getAsInt();
				int b = array.get(2).getAsInt();
				int a = 255;

				if (array.size() >= 3)
				{
					a = array.get(3).getAsInt();
				}

				return rgba(r, g, b, a);
			}
		}

		JsonObject object = element.getAsJsonObject();

		if (object.has("red") && object.has("green") && object.has("blue"))
		{
			int r = object.get("red").getAsInt();
			int g = object.get("green").getAsInt();
			int b = object.get("blue").getAsInt();
			int a = 255;

			if (object.has("alpha"))
			{
				a = object.get("alpha").getAsInt();
			}

			return rgba(r, g, b, a);
		}

		return Icon.EMPTY;
	}

	public static Color4I rgba(int r, int g, int b, int a)
	{
		return new Color4I(r, g, b, a);
	}

	public static Color4I rgb(int r, int g, int b)
	{
		return rgba(r, g, b, 255);
	}

	public static Color4I rgba(int col)
	{
		return rgba(ColorUtils.getRed(col), ColorUtils.getGreen(col), ColorUtils.getBlue(col), ColorUtils.getAlpha(col));
	}

	public static Color4I rgb(int col)
	{
		return rgb(ColorUtils.getRed(col), ColorUtils.getGreen(col), ColorUtils.getBlue(col));
	}

	public static final Color4I BLACK_A[] = new Color4I[256];
	public static final Color4I WHITE_A[] = new Color4I[256];

	static
	{
		for (int i = 0; i < 256; i++)
		{
			BLACK_A[i] = rgba(0, 0, 0, i);
			WHITE_A[i] = rgba(255, 255, 255, i);
		}
	}

	public static final Color4I BLACK = BLACK_A[255];
	public static final Color4I DARK_GRAY = rgb(0x212121);
	public static final Color4I GRAY = rgb(0x999999);
	public static final Color4I WHITE = WHITE_A[255];
	public static final Color4I RED = rgb(0xFF0000);
	public static final Color4I GREEN = rgb(0x00FF00);
	public static final Color4I BLUE = rgb(0x0000FF);
	public static final Color4I LIGHT_RED = rgb(0xFF5656);
	public static final Color4I LIGHT_GREEN = rgb(0x56FF56);
	public static final Color4I LIGHT_BLUE = rgb(0x5656FF);

	int red = 255, green = 255, blue = 255, alpha = 255, rgba = 0xFFFFFFFF;

	Color4I(int r, int g, int b, int a)
	{
		super();
		red = r;
		green = g;
		blue = b;
		alpha = a;
		rgba = ColorUtils.getRGBA(red, green, blue, alpha);
	}

	public Color4I copy()
	{
		return rgba(red, green, blue, alpha);
	}

	public boolean isMutable()
	{
		return false;
	}

	public MutableColor4I mutable()
	{
		return new MutableColor4I(red, green, blue, alpha);
	}

	public Color4I whiteIfEmpty()
	{
		return isEmpty() ? WHITE : this;
	}

	public int redi()
	{
		return red;
	}

	public int greeni()
	{
		return green;
	}

	public int bluei()
	{
		return blue;
	}

	public int alphai()
	{
		return alpha;
	}

	public float redf()
	{
		return red / 255F;
	}

	public float greenf()
	{
		return green / 255F;
	}

	public float bluef()
	{
		return blue / 255F;
	}

	public float alphaf()
	{
		return alpha / 255F;
	}

	public int rgba()
	{
		return rgba;
	}

	public int hashCode()
	{
		return rgba();
	}

	public boolean equals(Object o)
	{
		return o == this || (o != null && o.hashCode() == rgba());
	}

	public String toString()
	{
		return ColorUtils.getHex(rgba());
	}

	@Override
	public JsonElement getJson()
	{
		return isEmpty() ? JsonNull.INSTANCE : new JsonPrimitive(toString());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		if (w <= 0 || h <= 0)
		{
			return;
		}
		else if (col.isEmpty())
		{
			col = this;
		}

		if (col.isEmpty())
		{
			return;
		}

		GlStateManager.disableTexture2D();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		GuiHelper.addRectToBuffer(buffer, x, y, w, h, col);
		tessellator.draw();
		GlStateManager.enableTexture2D();
	}
}