package com.feed_the_beast.ftblib.lib.icon;

import com.feed_the_beast.ftblib.lib.ATHelper;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.math.MathUtils;
import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class Color4I extends Icon
{
	private static final Color4I BLACK_A[] = new Color4I[256];
	private static final Color4I WHITE_A[] = new Color4I[256];

	static
	{
		for (int i = 0; i < 256; i++)
		{
			BLACK_A[i] = new Color4I(0, 0, 0, i)
			{
				@Override
				public Color4I withAlpha(int a)
				{
					return alpha == a ? this : BLACK_A[a & 255];
				}
			};

			WHITE_A[i] = new Color4I(255, 255, 255, i)
			{
				@Override
				public Color4I withAlpha(int a)
				{
					return alpha == a ? this : WHITE_A[a & 255];
				}
			};
		}
	}

	public static final Color4I BLACK = rgb(0x000000);
	public static final Color4I DARK_GRAY = rgb(0x212121);
	public static final Color4I GRAY = rgb(0x999999);
	public static final Color4I WHITE = rgb(0xFFFFFF);
	public static final Color4I RED = rgb(0xFF0000);
	public static final Color4I GREEN = rgb(0x00FF00);
	public static final Color4I BLUE = rgb(0x0000FF);
	public static final Color4I LIGHT_RED = rgb(0xFF5656);
	public static final Color4I LIGHT_GREEN = rgb(0x56FF56);
	public static final Color4I LIGHT_BLUE = rgb(0x5656FF);
	private static final Color4I[] CHAT_FORMATTING_COLORS = new Color4I[16];
	private static final Color4I[] DYE_TEXT_FORMATTING_COLORS = new Color4I[32];

	static
	{
		for (int i = 0; i < 16; i++)
		{
			int j = (i >> 3 & 1) * 85;
			int r = (i >> 2 & 1) * 170 + j;
			int g = (i >> 1 & 1) * 170 + j;
			int b = (i & 1) * 170 + j;
			CHAT_FORMATTING_COLORS[i] = rgb((i == 6) ? r + 85 : r, g, b);
		}
	}

	public static Color4I getChatFormattingColor(int id)
	{
		return CHAT_FORMATTING_COLORS[id & 0xF];
	}

	public static Color4I getChatFormattingColor(@Nullable TextFormatting formatting)
	{
		return formatting == null ? WHITE : getChatFormattingColor(formatting.ordinal());
	}

	public static Color4I getDyeColor(EnumDyeColor color, boolean isLighter)
	{
		int id = color.ordinal();

		if (DYE_TEXT_FORMATTING_COLORS[id] == null)
		{
			char c = ATHelper.getTextFormattingChar(ATHelper.getTextFormattingFromDyeColor(color));
			DYE_TEXT_FORMATTING_COLORS[id] = rgb(GuiUtils.getColorCode(c, true));
			DYE_TEXT_FORMATTING_COLORS[id + 16] = rgb(GuiUtils.getColorCode(c, false));
		}

		return DYE_TEXT_FORMATTING_COLORS[id + (isLighter ? 0 : 16)];
	}

	public static Color4I fromJson(@Nullable JsonElement element)
	{
		if (JsonUtils.isNull(element))
		{
			return Icon.EMPTY;
		}
		else if (element.isJsonPrimitive())
		{
			String s = element.getAsString();

			if ((s.length() == 7 || s.length() == 9) && s.charAt(0) == '#')
			{
				String hex = s.substring(1);
				return hex.length() == 8 ? rgba((int) Long.parseLong(hex, 16)) : rgb((int) Long.parseLong(hex, 16));
			}

			return Icon.EMPTY;
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
		r = r & 255;
		g = g & 255;
		b = b & 255;
		a = a & 255;

		if (a == 0)
		{
			return EMPTY;
		}
		else if (r == 0 && g == 0 && b == 0)
		{
			return BLACK_A[a];
		}
		else if (r == 255 && g == 255 && b == 255)
		{
			return WHITE_A[a];
		}

		return new Color4I(r, g, b, a);
	}

	public static Color4I rgb(int r, int g, int b)
	{
		return rgba(r, g, b, 255);
	}

	public static Color4I hsb(float h, float s, float b)
	{
		return rgb(java.awt.Color.HSBtoRGB(h, s, b));
	}

	public static Color4I rgba(int col)
	{
		return rgba(col >> 16, col >> 8, col, col >> 24);
	}

	public static Color4I rgb(int col)
	{
		return rgb(col >> 16, col >> 8, col);
	}

	public static Color4I rgb(Vec3d color)
	{
		return rgb((int) (color.x * 255D), (int) (color.y * 255D), (int) (color.z * 255D));
	}

	int red, green, blue, alpha;

	Color4I(int r, int g, int b, int a)
	{
		red = r;
		green = g;
		blue = b;
		alpha = a;
	}

	@Override
	public Color4I copy()
	{
		return this;
	}

	public boolean isMutable()
	{
		return false;
	}

	public MutableColor4I mutable()
	{
		return new MutableColor4I(redi(), greeni(), bluei(), alphai());
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
		return redi() / 255F;
	}

	public float greenf()
	{
		return greeni() / 255F;
	}

	public float bluef()
	{
		return bluei() / 255F;
	}

	public float alphaf()
	{
		return alphai() / 255F;
	}

	public int rgba()
	{
		return (alphai() << 24) | (redi() << 16) | (greeni() << 8) | bluei();
	}

	public final int hashCode()
	{
		return rgba();
	}

	public boolean equals(Object o)
	{
		return o == this || (o instanceof Color4I && o.hashCode() == rgba());
	}

	public String toString()
	{
		int a = alphai();
		char[] chars;

		if (a < 255)
		{
			chars = new char[9];
			chars[1] = CommonUtils.HEX[(a & 0xF0) >> 4];
			chars[2] = CommonUtils.HEX[a & 0xF];
			int r = redi();
			chars[3] = CommonUtils.HEX[(r & 0xF0) >> 4];
			chars[4] = CommonUtils.HEX[r & 0xF];
			int g = greeni();
			chars[5] = CommonUtils.HEX[(g & 0xF0) >> 4];
			chars[6] = CommonUtils.HEX[g & 0xF];
			int b = bluei();
			chars[7] = CommonUtils.HEX[(b & 0xF0) >> 4];
			chars[8] = CommonUtils.HEX[b & 0xF];
		}
		else
		{
			chars = new char[7];
			int r = redi();
			chars[1] = CommonUtils.HEX[(r & 0xF0) >> 4];
			chars[2] = CommonUtils.HEX[r & 0xF];
			int g = greeni();
			chars[3] = CommonUtils.HEX[(g & 0xF0) >> 4];
			chars[4] = CommonUtils.HEX[g & 0xF];
			int b = bluei();
			chars[5] = CommonUtils.HEX[(b & 0xF0) >> 4];
			chars[6] = CommonUtils.HEX[b & 0xF];
		}

		chars[0] = '#';
		return new String(chars);
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
		else if (!col.isEmpty())
		{
			col = withTint(col);

			if (col.isEmpty())
			{
				return;
			}
		}
		else
		{
			col = this;
		}

		GlStateManager.disableTexture2D();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		GuiHelper.addRectToBuffer(buffer, x, y, w, h, col);
		tessellator.draw();
		GlStateManager.enableTexture2D();
	}

	@Override
	public Color4I withTint(Color4I col)
	{
		if (isEmpty())
		{
			return this;
		}
		else if (col.isEmpty())
		{
			return EMPTY;
		}
		else if (col.redi() == 255 && col.greeni() == 255 && col.bluei() == 255)
		{
			return this;
		}

		double a = col.alphaf();
		double r = MathUtils.lerp(redi(), col.redi(), a);
		double g = MathUtils.lerp(greeni(), col.greeni(), a);
		double b = MathUtils.lerp(bluei(), col.bluei(), a);
		return rgba((int) r, (int) g, (int) b, alpha);
	}

	public Color4I withAlpha(int a)
	{
		return alpha == a ? this : rgba(redi(), greeni(), bluei(), a);
	}

	public final Color4I withAlphaf(float alpha)
	{
		return withAlpha((int) (alpha * 255F));
	}

	public Color4I lerp(Color4I col, float m)
	{
		m = MathHelper.clamp(m, 0F, 1F);
		float r = MathUtils.lerp(redf(), col.redf(), m);
		float g = MathUtils.lerp(greenf(), col.greenf(), m);
		float b = MathUtils.lerp(bluef(), col.bluef(), m);
		float a = MathUtils.lerp(alphaf(), col.alphaf(), m);
		return rgba((int) (r * 255F), (int) (g * 255F), (int) (b * 255F), (int) (a * 255F));
	}

	public Color4I addBrightness(float percent)
	{
		float[] hsb = new float[3];
		java.awt.Color.RGBtoHSB(redi(), greeni(), bluei(), hsb);
		return rgb(java.awt.Color.HSBtoRGB(hsb[0], hsb[1], MathHelper.clamp(hsb[2] + percent, 0F, 1F))).withAlpha(alphai());
	}
}