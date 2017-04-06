package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.lib.util.LMColorUtils;

/**
 * @author LatvianModder
 */
public class ImmutableColor4I extends Color4I
{
    public ImmutableColor4I(int r, int g, int b, int a)
    {
        super(r, g, b, a);
    }

    public ImmutableColor4I(int col)
    {
        this(LMColorUtils.getRed(col), LMColorUtils.getGreen(col), LMColorUtils.getBlue(col), LMColorUtils.getAlpha(col));
    }

    public ImmutableColor4I(Color4I col)
    {
        this(col.red(), col.green(), col.blue(), col.alpha());
    }

    @Override
    protected boolean canSetColor(int r, int g, int b, int a)
    {
        return false;
    }
}