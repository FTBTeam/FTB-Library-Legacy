package com.latmod.lib.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.latmod.lib.math.MathHelperLM;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public class LMColorUtils
{
    public static final int[] chatFormattingColors = new int[16];

    public static final int BLACK = 0xFF000000;
    public static final int WHITE = 0xFFFFFFFF;
    public static final int LIGHT_GRAY = 0xFFAAAAAA;
    public static final int DARK_GRAY = 0xFF333333;
    public static final int WIDGETS = 0xFF000000;

    static
    {
        for(int i = 0; i < 16; i++)
        {
            int j = (i >> 3 & 1) * 85;
            int r = (i >> 2 & 1) * 170 + j;
            int g = (i >> 1 & 1) * 170 + j;
            int b = (i & 1) * 170 + j;
            if(i == 6)
            {
                r += 85;
            }
            chatFormattingColors[i] = getRGBA(r, g, b, 255);
        }
    }

    public static JsonElement serialize(int col)
    {
        return new JsonPrimitive('#' + Integer.toHexString(col).toUpperCase());
    }

    public static int deserialize(@Nullable JsonElement e)
    {
        if(e == null || !e.isJsonPrimitive())
        {
            return 0xFF000000;
        }

        return (int) Long.parseLong(e.getAsString().substring(1), 16);
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

    public static int addBrightness(int c, int b)
    {
        int red = MathHelper.clamp_int(getRed(c) + b, 0, 255);
        int green = MathHelper.clamp_int(getGreen(c) + b, 0, 255);
        int blue = MathHelper.clamp_int(getBlue(c) + b, 0, 255);
        return getRGBA(red, green, blue, getAlpha(c));
    }

    public static void addHSB(int pixels[], float h, float s, float b)
    {
        if(pixels.length > 0)
        {
            float[] hsb = new float[3];

            for(int i = 0; i < pixels.length; i++)
            {
                java.awt.Color.RGBtoHSB(getRed(pixels[i]), getGreen(pixels[i]), getBlue(pixels[i]), hsb);
                hsb[0] += h;
                hsb[1] = MathHelper.clamp_float(hsb[1] + s, 0F, 1F);
                hsb[2] = MathHelper.clamp_float(hsb[2] + b, 0F, 1F);
                pixels[i] = getRGBA(java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]), 255);
            }
        }
    }

    public static int lerp(int col1, int col2, double m, int alpha)
    {
        m = MathHelper.clamp_double(m, 0D, 1D);
        int r = MathHelperLM.lerp_int(getRed(col1), getRed(col2), m);
        int g = MathHelperLM.lerp_int(getGreen(col1), getGreen(col2), m);
        int b = MathHelperLM.lerp_int(getBlue(col1), getBlue(col2), m);
        return getRGBA(r, g, b, alpha);
    }

    public static int lerp(int col1, int col2, double m)
    {
        return lerp(col1, col2, m, getAlpha(col1));
    }

    public static int multiply(int col1, int col2, int a)
    {
        float r = MathHelper.clamp_float(getRedF(col1) * getRedF(col2), 0F, 1F);
        float g = MathHelper.clamp_float(getGreenF(col1) * getGreenF(col2), 0F, 1F);
        float b = MathHelper.clamp_float(getBlueF(col1) * getBlueF(col2), 0F, 1F);
        return getRGBA((int) (r * 255F), (int) (g * 255F), (int) (b * 255F), a);
    }
}