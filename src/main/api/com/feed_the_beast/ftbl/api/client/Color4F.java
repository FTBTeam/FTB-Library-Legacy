package com.feed_the_beast.ftbl.api.client;

import com.latmod.lib.util.LMColorUtils;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public final class Color4F
{
    public float red, green, blue, alpha;

    public Color4F()
    {
        this(1F, 1F, 1F, 1F);
    }

    public Color4F(float r, float g, float b, float a)
    {
        set(r, g, b, a);
    }

    public void set(float r, float g, float b, float a)
    {
        red = r;
        green = g;
        blue = b;
        alpha = a;
    }

    public void set(Color4F col)
    {
        set(col.red, col.green, col.blue, col.alpha);
    }

    public void set(int col)
    {
        set(col, LMColorUtils.getAlphaF(col));
    }

    public void set(int col, float a)
    {
        red = LMColorUtils.getRedF(col);
        green = LMColorUtils.getGreenF(col);
        blue = LMColorUtils.getBlueF(col);
        alpha = a;
    }
}