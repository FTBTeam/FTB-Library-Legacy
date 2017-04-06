package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.lib.util.LMColorUtils;
import com.google.gson.JsonElement;
import net.minecraft.util.math.MathHelper;

/**
 * @author LatvianModder
 */
public class Color4I
{
    public static final Color4I NONE = new ImmutableColor4I(0x00000000);

    public static final Color4I BLACK = new ImmutableColor4I(0xFF000000);
    public static final Color4I DARK_GRAY = new ImmutableColor4I(0xFF212121);
    public static final Color4I GRAY = new ImmutableColor4I(0xFF999999);
    public static final Color4I WHITE = new ImmutableColor4I(0xFFFFFFFF);
    public static final Color4I WHITE_A33 = new ImmutableColor4I(0x21FFFFFF);

    public static final Color4I RED = new ImmutableColor4I(0xFFFF0000);
    public static final Color4I LIGHT_RED = new ImmutableColor4I(0xFFFF5656);

    private int red = 255, green = 255, blue = 255, alpha = 255, rgba = 0xFFFFFFFF;

    public Color4I()
    {
    }

    public Color4I(int r, int g, int b, int a)
    {
        red = r;
        green = g;
        blue = b;
        alpha = a;
        rgba = LMColorUtils.getRGBA(red, green, blue, alpha);
    }

    public Color4I(int col)
    {
        red = LMColorUtils.getRed(col);
        green = LMColorUtils.getGreen(col);
        blue = LMColorUtils.getBlue(col);
        alpha = LMColorUtils.getAlpha(col);
        rgba = col;
    }

    public Color4I(Color4I col)
    {
        this(col.red, col.green, col.blue, col.alpha);
    }

    protected boolean canSetColor(int r, int g, int b, int a)
    {
        return true;
    }

    protected void onColorSet()
    {
    }

    public final Color4I set(int r, int g, int b, int a)
    {
        if(canSetColor(r, g, b, a))
        {
            red = r;
            green = g;
            blue = b;
            alpha = a;
            rgba = LMColorUtils.getRGBA(red, green, blue, alpha);
            onColorSet();
        }

        return this;
    }

    public final Color4I set(Color4I col, int a)
    {
        return set(col.red, col.green, col.blue, a);
    }

    public final Color4I set(Color4I col)
    {
        return set(col, col.alpha);
    }

    public final Color4I set(int col, int a)
    {
        return set(LMColorUtils.getRed(col), LMColorUtils.getGreen(col), LMColorUtils.getBlue(col), a);
    }

    public final Color4I set(int col)
    {
        return set(col, LMColorUtils.getAlpha(col));
    }

    public final int red()
    {
        return red;
    }

    public final int green()
    {
        return green;
    }

    public final int blue()
    {
        return blue;
    }

    public final int alpha()
    {
        return alpha;
    }

    public final float redf()
    {
        return red / 255F;
    }

    public final float greenf()
    {
        return green / 255F;
    }

    public final float bluef()
    {
        return blue / 255F;
    }

    public final float alphaf()
    {
        return alpha / 255F;
    }

    public final int rgba()
    {
        return rgba;
    }

    public final boolean hasColor()
    {
        return alpha > 0;
    }

    public final Color4I copy(boolean immutable, int a)
    {
        return immutable ? new ImmutableColor4I(red, green, blue, a) : new Color4I(red, green, blue, a);
    }

    public final Color4I copy(boolean immutable)
    {
        return copy(immutable, alpha);
    }

    public final int hashCode()
    {
        return rgba();
    }

    public final boolean equals(Object o)
    {
        return o == this || (o != null && o.hashCode() == rgba());
    }

    public final String toString()
    {
        return LMColorUtils.getHex(rgba());
    }

    public final JsonElement toJson()
    {
        return LMColorUtils.serialize(rgba());
    }

    public void addBrightness(int b)
    {
        set(MathHelper.clamp(red + b, 0, 255), MathHelper.clamp(green + b, 0, 255), MathHelper.clamp(blue + b, 0, 255), alpha);
    }
}