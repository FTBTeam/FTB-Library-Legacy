package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.lib.util.ColorUtils;
import com.google.gson.JsonElement;
import net.minecraft.util.math.MathHelper;

/**
 * @author LatvianModder
 */
public final class Color4I
{
    public static final Color4I NONE = new Color4I(false, 0x00000000);

    public static final Color4I BLACK = new Color4I(false, 0xFF000000);
    public static final Color4I DARK_GRAY = new Color4I(false, 0xFF212121);
    public static final Color4I GRAY = new Color4I(false, 0xFF999999);
    public static final Color4I WHITE = new Color4I(false, 0xFFFFFFFF);
    public static final Color4I WHITE_A33 = new Color4I(false, 0x21FFFFFF);

    public static final Color4I RED = new Color4I(false, 0xFFFF0000);
    public static final Color4I LIGHT_RED = new Color4I(false, 0xFFFF5656);

    private final boolean canEdit;
    private int red = 255, green = 255, blue = 255, alpha = 255, rgba = 0xFFFFFFFF;

    public Color4I(boolean canEdit)
    {
        this.canEdit = canEdit;
    }

    public Color4I(boolean canEdit, int r, int g, int b, int a)
    {
        this.canEdit = canEdit;
        red = r;
        green = g;
        blue = b;
        alpha = a;
        rgba = ColorUtils.getRGBA(red, green, blue, alpha);
    }

    public Color4I(boolean canEdit, int rgba)
    {
        this.canEdit = canEdit;
        red = ColorUtils.getRed(rgba);
        green = ColorUtils.getGreen(rgba);
        blue = ColorUtils.getBlue(rgba);
        alpha = ColorUtils.getAlpha(rgba);
        this.rgba = rgba;
    }

    public Color4I(boolean canEdit, Color4I col, int a)
    {
        this(canEdit, col.red, col.green, col.blue, a);
    }

    public Color4I(boolean canEdit, Color4I col)
    {
        this(canEdit, col, col.alpha);
    }

    public boolean canEdit()
    {
        return canEdit;
    }

    public void set(int r, int g, int b, int a)
    {
        if(canEdit)
        {
            red = r;
            green = g;
            blue = b;
            alpha = a;
            rgba = ColorUtils.getRGBA(red, green, blue, alpha);
        }
    }

    public void set(Color4I col, int a)
    {
        if(canEdit)
        {
            set(col.red, col.green, col.blue, a);
        }
    }

    public void set(Color4I col)
    {
        if(canEdit)
        {
            set(col, col.alpha);
        }
    }

    public void set(int col, int a)
    {
        if(canEdit)
        {
            set(ColorUtils.getRed(col), ColorUtils.getGreen(col), ColorUtils.getBlue(col), a);
        }
    }

    public void set(int col)
    {
        if(canEdit)
        {
            set(col, ColorUtils.getAlpha(col));
        }
    }

    public int red()
    {
        return red;
    }

    public int green()
    {
        return green;
    }

    public int blue()
    {
        return blue;
    }

    public int alpha()
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
        return alpha > 0;
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
        return ColorUtils.serialize(rgba());
    }

    public void addBrightness(int b)
    {
        if(canEdit)
        {
            set(MathHelper.clamp(red + b, 0, 255), MathHelper.clamp(green + b, 0, 255), MathHelper.clamp(blue + b, 0, 255), alpha);
        }
    }
}