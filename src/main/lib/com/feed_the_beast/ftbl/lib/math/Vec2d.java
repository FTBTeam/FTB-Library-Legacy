package com.feed_the_beast.ftbl.lib.math;

import com.feed_the_beast.ftbl.lib.util.StringUtils;
import net.minecraft.util.math.MathHelper;

public class Vec2d
{
	public double x, y;

	public Vec2d()
	{
	}

	public Vec2d(double px, double py)
	{
		set(px, py);
	}

	public void set(double px, double py)
	{
		x = px;
		y = py;
	}

	public Vec2i toPos2I()
	{
		return new Vec2i(MathHelper.floor(x), MathHelper.floor(y));
	}

	public int hashCode()
	{
		return Double.hashCode(x) * 31 + Double.hashCode(y);
	}

	public boolean equalsPos(Vec2d o)
	{
		return o.x == x && o.y == y;
	}

	public String toString()
	{
		return "[" + StringUtils.formatDouble(x) + ',' + ' ' + StringUtils.formatDouble(y) + ']';
	}

	public boolean equals(Object o)
	{
		return o != null && (o == this || (o instanceof Vec2d && equalsPos((Vec2d) o)));
	}

	public Vec2d copy()
	{
		return new Vec2d(x, y);
	}
}