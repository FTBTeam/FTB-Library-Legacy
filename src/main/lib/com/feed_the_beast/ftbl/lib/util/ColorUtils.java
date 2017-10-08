package com.feed_the_beast.ftbl.lib.util;

import com.feed_the_beast.ftbl.lib.icon.Color4I;
import com.feed_the_beast.ftbl.lib.math.MathUtils;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

public class ColorUtils
{
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
			CHAT_FORMATTING_COLORS[i] = Color4I.rgb((i == 6) ? r + 85 : r, g, b);
		}

		for (EnumDyeColor color : EnumDyeColor.values())
		{
			char c = getTextFormattingChar(getFromDyeColor(color));
			DYE_TEXT_FORMATTING_COLORS[color.getMetadata()] = Color4I.rgb(GuiUtils.getColorCode(c, true));
			DYE_TEXT_FORMATTING_COLORS[color.getMetadata() + 16] = Color4I.rgb(GuiUtils.getColorCode(c, false));
		}
	}

	public static TextFormatting getFromDyeColor(EnumDyeColor color)
	{
		return color.chatColor;
	}

	public static char getTextFormattingChar(TextFormatting formatting)
	{
		return formatting.formattingCode;
	}

	public static Color4I getChatFormattingColor(int id)
	{
		return CHAT_FORMATTING_COLORS[id & 0xF];
	}

	public static Color4I getDyeColor(EnumDyeColor color, boolean isLighter)
	{
		return DYE_TEXT_FORMATTING_COLORS[color.getMetadata() + (isLighter ? 0 : 16)];
	}

	public static int getRGBA(int r, int g, int b, int a)
	{
		return ((a & 255) << 24) | ((r & 255) << 16) | ((g & 255) << 8) | ((b & 255));
	}

	public static int getRGBAF(float r, float g, float b, float a)
	{
		return getRGBA((int) (r * 255F), (int) (g * 255F), (int) (b * 255F), (int) (a * 255F));
	}

	public static int getRed(int c)
	{
		return (c >> 16) & 255;
	}

	public static int getGreen(int c)
	{
		return (c >> 8) & 255;
	}

	public static int getBlue(int c)
	{
		return (c) & 255;
	}

	public static int getAlpha(int c)
	{
		return (c >> 24) & 255;
	}

	public static float getRedF(int c)
	{
		return getRed(c) / 255F;
	}

	public static float getGreenF(int c)
	{
		return getGreen(c) / 255F;
	}

	public static float getBlueF(int c)
	{
		return getBlue(c) / 255F;
	}

	public static float getAlphaF(int c)
	{
		return getAlpha(c) / 255F;
	}

	public static String getHex(int c)
	{
		return '#' + Integer.toHexString(getRGBA(c, 255)).substring(2).toUpperCase();
	}

	public static int getRGBA(int c, int a)
	{
		return getRGBA(getRed(c), getGreen(c), getBlue(c), a);
	}

	public static ByteBuffer toByteBuffer(int pixels[], boolean alpha)
	{
		ByteBuffer bb = BufferUtils.createByteBuffer(pixels.length * 4);
		byte alpha255 = (byte) 255;

		for (int p : pixels)
		{
			bb.put((byte) getRed(p));
			bb.put((byte) getGreen(p));
			bb.put((byte) getBlue(p));
			bb.put(alpha ? (byte) getAlpha(p) : alpha255);
		}

		bb.flip();
		return bb;
	}

	public static int addBrightness(int c, float percent)
	{
		float[] hsb = new float[3];
		java.awt.Color.RGBtoHSB(getRed(c), getGreen(c), getBlue(c), hsb);
		return (getAlpha(c) << 24) | java.awt.Color.HSBtoRGB(hsb[0], hsb[1], MathHelper.clamp(hsb[2] + percent, 0F, 1F));
	}

	public static void addHSB(int pixels[], float h, float s, float b)
	{
		if (pixels.length > 0)
		{
			float[] hsb = new float[3];

			for (int i = 0; i < pixels.length; i++)
			{
				java.awt.Color.RGBtoHSB(getRed(pixels[i]), getGreen(pixels[i]), getBlue(pixels[i]), hsb);
				hsb[0] += h;
				hsb[1] = MathHelper.clamp(hsb[1] + s, 0F, 1F);
				hsb[2] = MathHelper.clamp(hsb[2] + b, 0F, 1F);
				pixels[i] = getRGBA(java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]), 255);
			}
		}
	}

	public static int lerp(int col1, int col2, double m, int alpha)
	{
		m = MathHelper.clamp(m, 0D, 1D);
		int r = MathUtils.lerp(getRed(col1), getRed(col2), m);
		int g = MathUtils.lerp(getGreen(col1), getGreen(col2), m);
		int b = MathUtils.lerp(getBlue(col1), getBlue(col2), m);
		return getRGBA(r, g, b, alpha);
	}

	public static int lerp(int col1, int col2, double m)
	{
		return lerp(col1, col2, m, getAlpha(col1));
	}

	public static int multiply(int col1, int col2, int a)
	{
		float r = MathHelper.clamp(getRedF(col1) * getRedF(col2), 0F, 1F);
		float g = MathHelper.clamp(getGreenF(col1) * getGreenF(col2), 0F, 1F);
		float b = MathHelper.clamp(getBlueF(col1) * getBlueF(col2), 0F, 1F);
		return getRGBA((int) (r * 255F), (int) (g * 255F), (int) (b * 255F), a);
	}
}