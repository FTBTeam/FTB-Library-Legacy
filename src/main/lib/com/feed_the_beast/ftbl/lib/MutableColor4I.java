package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.lib.util.ColorUtils;
import net.minecraft.util.math.MathHelper;

/**
 * @author LatvianModder
 */
public class MutableColor4I extends Color4I
{
	public static final Color4I TEMP = new MutableColor4I(255, 255, 255, 255);

	static class None extends MutableColor4I
	{
		private boolean hasColor = false;

		None()
		{
			super(255, 255, 255, 255);
		}

		@Override
		public void set(int r, int g, int b, int a)
		{
			hasColor = true;
			super.set(r, g, b, a);
		}

		@Override
		public boolean hasColor()
		{
			return hasColor;
		}
	}

	MutableColor4I(int r, int g, int b, int a)
	{
		super(r, g, b, a);
	}

	@Override
	public MutableColor4I copy()
	{
		return new MutableColor4I(red, green, blue, alpha);
	}

	@Override
	public boolean isMutable()
	{
		return true;
	}

	@Override
	public MutableColor4I mutable()
	{
		return this;
	}

	public void set(int r, int g, int b, int a)
	{
		red = r;
		green = g;
		blue = b;
		alpha = a;
		rgba = ColorUtils.getRGBA(red, green, blue, alpha);
	}

	public void set(Color4I col, int a)
	{
		set(col.red, col.green, col.blue, a);
	}

	public void set(Color4I col)
	{
		set(col, col.alpha);
	}

	public void set(int col, int a)
	{
		set(ColorUtils.getRed(col), ColorUtils.getGreen(col), ColorUtils.getBlue(col), a);
	}

	public void set(int col)
	{
		set(col, ColorUtils.getAlpha(col));
	}

	public void setAlpha(int a)
	{
		alpha = a;
	}

	public void addBrightness(int b)
	{
		set(MathHelper.clamp(red + b, 0, 255), MathHelper.clamp(green + b, 0, 255), MathHelper.clamp(blue + b, 0, 255), alpha);
	}

	private static int toint(float f)
	{
		return (int) (f * 255F + 0.5F);
	}

	public void setFromHSB(float h, float s, float b)
	{
		red = green = blue = 0;
		if (s == 0)
		{
			red = green = blue = toint(b);
			return;
		}

		float h6 = (h - MathHelper.floor(h)) * 6F;
		float f = h6 - MathHelper.floor(h6);
		float p = b * (1F - s);
		float q = b * (1F - s * f);
		float t = b * (1F - (s * (1F - f)));
		switch ((int) h6)
		{
			case 0:
				red = toint(b);
				green = toint(t);
				blue = toint(p);
				break;
			case 1:
				red = toint(q);
				green = toint(b);
				blue = toint(p);
				break;
			case 2:
				red = toint(p);
				green = toint(b);
				blue = toint(t);
				break;
			case 3:
				red = toint(p);
				green = toint(q);
				blue = toint(b);
				break;
			case 4:
				red = toint(t);
				green = toint(p);
				blue = toint(b);
				break;
			case 5:
				red = toint(b);
				green = toint(p);
				blue = toint(q);
				break;
		}
	}
}