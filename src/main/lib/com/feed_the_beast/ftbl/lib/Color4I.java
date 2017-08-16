package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.lib.util.ColorUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class Color4I
{
	public static final Color4I NONE = new Color4I(255, 255, 255, 255)
	{
		@Override
		public boolean hasColor()
		{
			return false;
		}

		@Override
		public MutableColor4I mutable()
		{
			return new MutableColor4I.None();
		}
	};

	public static Color4I fromJson(@Nullable JsonElement element)
	{
		if (element == null || element.isJsonNull())
		{
			return NONE;
		}
		else if (element.isJsonPrimitive())
		{
			String s = element.getAsString();

			if (s.equals("-"))
			{
				return NONE;
			}
			else
			{
				String hex = element.getAsString().substring(1);
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

		return NONE;
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
	public static final Color4I GREEN = rgb(0xFF0000);
	public static final Color4I BLUE = rgb(0xFF0000);
	public static final Color4I LIGHT_RED = rgb(0xFFFF5656);
	public static final Color4I LIGHT_GREEN = rgb(0xFFFF5656);
	public static final Color4I LIGHT_BLUE = rgb(0xFFFF5656);

	int red = 255, green = 255, blue = 255, alpha = 255, rgba = 0xFFFFFFFF;

	Color4I(int r, int g, int b, int a)
	{
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

	public boolean hasColor()
	{
		return true;
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

	public JsonElement toJson()
	{
		return hasColor() ? new JsonPrimitive(toString()) : JsonNull.INSTANCE;
	}
}