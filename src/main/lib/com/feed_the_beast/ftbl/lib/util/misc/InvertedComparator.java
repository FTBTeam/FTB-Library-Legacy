package com.feed_the_beast.ftbl.lib.util.misc;

import java.util.Comparator;

/**
 * @author LatvianModder
 */
public class InvertedComparator<T> implements Comparator<T>
{
	private final Comparator<T> comparator;

	public InvertedComparator(Comparator<T> c)
	{
		comparator = c;
	}

	@Override
	public int compare(T o1, T o2)
	{
		return comparator.compare(o2, o1);
	}
}