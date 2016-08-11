package com.latmod.lib.math;

import javax.annotation.Nonnull;

public class Pos2I
{
    public int x, y;

    public Pos2I()
    {
    }

    public Pos2I(int px, int py)
    {
        set(px, py);
    }

    public void set(int px, int py)
    {
        x = px;
        y = py;
    }

    @Nonnull
    public Pos2D toPos2D()
    {
        return new Pos2D(x, y);
    }

    @Override
    public int hashCode()
    {
        return x * 31 + y;
    }

    public boolean equalsPos(@Nonnull Pos2I o)
    {
        return o.x == x && o.y == y;
    }

    @Override
    public String toString()
    {
        return "[" + x + ',' + ' ' + y + ']';
    }

    @Override
    public boolean equals(Object o)
    {
        return o != null && (o == this || (o instanceof Pos2I && equalsPos((Pos2I) o)));
    }

    @Nonnull
    public Pos2I copy()
    {
        return new Pos2I(x, y);
    }
}