package com.feed_the_beast.ftbl.lib.client;

import com.feed_the_beast.ftbl.lib.util.LMColorUtils;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public final class Color4I
{
    public int red, green, blue, alpha;

    public Color4I()
    {
        this(255, 255, 255, 255);
    }

    public Color4I(int r, int g, int b, int a)
    {
        set(r, g, b, a);
    }

    public void set(int r, int g, int b, int a)
    {
        red = r;
        green = g;
        blue = b;
        alpha = a;
    }

    public void set(Color4I col)
    {
        set(col.red, col.green, col.blue, col.alpha);
    }

    public void set(int col)
    {
        set(col, LMColorUtils.getAlpha(col));
    }

    public void set(int col, int a)
    {
        red = LMColorUtils.getRed(col);
        green = LMColorUtils.getGreen(col);
        blue = LMColorUtils.getBlue(col);
        alpha = a;
    }
}