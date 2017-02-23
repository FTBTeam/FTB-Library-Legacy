package com.feed_the_beast.ftbl.lib.util;

import com.feed_the_beast.ftbl.lib.client.Color4I;
import com.feed_the_beast.ftbl.lib.math.MathHelperLM;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;

import javax.annotation.Nullable;
import java.awt.*;
import java.nio.ByteBuffer;

public class LMColorUtils
{
    private static final int[] CHAT_FORMATTING_COLORS = new int[16];
    private static final int[] ID_COLORS = new int[256];
    private static final int[] DYE_TEXT_FORMATTING_COLORS = new int[32];

    static
    {
        for(int i = 0; i < 16; i++)
        {
            int j = (i >> 3 & 1) * 85;
            int r = (i >> 2 & 1) * 170 + j;
            int g = (i >> 1 & 1) * 170 + j;
            int b = (i & 1) * 170 + j;
            CHAT_FORMATTING_COLORS[i] = getRGBA((i == 6) ? r + 85 : r, g, b, 255);
        }

        for(int y = 0; y < 16; y++)
        {
            for(int x = 0; x < 16; x++)
            {
                if(x == 0)
                {
                    if(y > 0)
                    {
                        float ry = y == 1 ? 0F : (y == 15) ? 1F : ((y - 1F) / 15F);
                        ID_COLORS[y * 16] = 0xFF000000 | getRGBAF(ry, ry, ry, 1F);
                    }
                }
                else
                {
                    float h = (x - 1F) / 15F;
                    float b = 1F;
                    float s = 1F;

                    if(y < 8)
                    {
                        b = 0.2F + (y / 8F) * 0.8F;
                    }
                    else if(y > 8)
                    {
                        s = 1F - (0.2F + ((y - 8) / 9F) * 0.8F);
                    }

                    ID_COLORS[x + y * 16] = 0xFF000000 | Color.HSBtoRGB(h, s, b);
                }
            }
        }

        ID_COLORS[0] = 0;

        for(EnumDyeColor color : EnumDyeColor.values())
        {
            char c = getTextFormattingChar(getFromDyeColor(color));
            DYE_TEXT_FORMATTING_COLORS[color.getMetadata()] = GuiUtils.getColorCode(c, true);
            DYE_TEXT_FORMATTING_COLORS[color.getMetadata() + 16] = GuiUtils.getColorCode(c, false);
        }
    }

    @SideOnly(Side.CLIENT)
    public static final Color4I GL_COLOR = new Color4I()
    {
        @Override
        public Color4I set(int r, int g, int b, int a)
        {
            GlStateManager.color(r / 255F, g / 255F, b / 255F, a / 255F);
            return this;
        }
    };

    public static int getColorFromID(byte col)
    {
        return ID_COLORS[col & 0xFF];
    }

    public static int getChatFormattingColor(int id)
    {
        return CHAT_FORMATTING_COLORS[id & 0xF];
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
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 255));
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

        for(int p : pixels)
        {
            bb.put((byte) getRed(p));
            bb.put((byte) getGreen(p));
            bb.put((byte) getBlue(p));
            bb.put(alpha ? (byte) getAlpha(p) : alpha255);
        }

        bb.flip();
        return bb;
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

    public static TextFormatting getFromDyeColor(EnumDyeColor color)
    {
        return color.chatColor;
    }

    public static char getTextFormattingChar(TextFormatting formatting)
    {
        return formatting.formattingCode;
    }

    public static int getDyeColor(EnumDyeColor color, boolean isLighter)
    {
        return DYE_TEXT_FORMATTING_COLORS[color.getMetadata() + (isLighter ? 0 : 16)];
    }
}