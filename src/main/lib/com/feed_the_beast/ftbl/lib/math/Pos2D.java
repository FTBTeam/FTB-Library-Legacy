package com.feed_the_beast.ftbl.lib.math;

import com.feed_the_beast.ftbl.lib.util.StringUtils;
import net.minecraft.util.math.MathHelper;

public class Pos2D
{
	public double x, y;

	public Pos2D()
	{
	}

	public Pos2D(double px, double py)
	{
		set(px, py);
	}

	public void set(double px, double py)
	{
		x = px;
		y = py;
	}

	public Pos2I toPos2I()
	{
		return new Pos2I(MathHelper.floor(x), MathHelper.floor(y));
	}

	public int hashCode()
	{
		return Double.hashCode(x) * 31 + Double.hashCode(y);
	}

	public boolean equalsPos(Pos2D o)
	{
		return o.x == x && o.y == y;
	}

	public String toString()
	{
		return "[" + StringUtils.formatDouble(x) + ',' + ' ' + StringUtils.formatDouble(y) + ']';
	}

	public boolean equals(Object o)
	{
		return o != null && (o == this || (o instanceof Pos2D && equalsPos((Pos2D) o)));
	}

	public Pos2D copy()
	{
		return new Pos2D(x, y);
	}
}