package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.lib.util.LMUtils;

/**
 * @author LatvianModder
 */
public final class Pushable<T>
{
	private final T[] data;
	private int index = 0;

	public Pushable(int max)
	{
		data = LMUtils.cast(new Object[max]);
	}

	public T get()
	{
		return data[index];
	}

	public void set(T v)
	{
		data[index] = v;
	}

	public void push()
	{
		index++;

		if (index >= data.length)
		{
			throw new ArrayIndexOutOfBoundsException("Index >= " + data.length + "!");
		}
	}

	public void pop()
	{
		index--;

		if (index < 0)
		{
			throw new ArrayIndexOutOfBoundsException("Index < 0!");
		}
	}

	public void reset()
	{
		index = 0;
	}

	public int getIndex()
	{
		return index;
	}
}