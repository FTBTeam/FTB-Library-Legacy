package com.feed_the_beast.ftbl.lib.math;

public class Vec2i
{
	public int x, y;

	public Vec2i()
	{
	}

	public Vec2i(int px, int py)
	{
		set(px, py);
	}

	public void set(int px, int py)
	{
		x = px;
		y = py;
	}

	public Vec2d toPos2D()
	{
		return new Vec2d(x, y);
	}

	public int hashCode()
	{
		return x * 31 + y;
	}

	public boolean equalsPos(Vec2i o)
	{
		return o.x == x && o.y == y;
	}

	public String toString()
	{
		return "[" + x + ',' + ' ' + y + ']';
	}

	public boolean equals(Object o)
	{
		return o != null && (o == this || (o instanceof Vec2i && equalsPos((Vec2i) o)));
	}

	public Vec2i copy()
	{
		return new Vec2i(x, y);
	}
}