package com.feed_the_beast.ftbl.lib.client;

import com.feed_the_beast.ftbl.lib.util.LMColorUtils;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public class Color4I
{
    private int red = 255, green = 255, blue = 255, alpha = 255;

    public Color4I set(int r, int g, int b, int a)
    {
        red = r;
        green = g;
        blue = b;
        alpha = a;
        return this;
    }

    public final Color4I set(Color4I col)
    {
        return set(col.red, col.green, col.blue, col.alpha);
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
}