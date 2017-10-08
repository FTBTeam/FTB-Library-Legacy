package com.feed_the_beast.ftbl.lib.util.misc;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public final class Pushable<E> implements Supplier<E>
{
	public final E defaultValue;
	private final Consumer<E> consumer;

	private E value;
	private final Queue<E> pushedValues;

	public Pushable(E def, Consumer<E> _set)
	{
		defaultValue = def;
		value = def;
		consumer = _set;
		pushedValues = new ArrayDeque<>(3);
		consumer.accept(defaultValue);
	}

	public void set(@Nullable E val)
	{
		if (val == null)
		{
			val = defaultValue;
		}

		if (!Objects.equals(value, val))
		{
			value = val;
			consumer.accept(value);
		}
	}

	public void reset()
	{
		set(defaultValue);
	}

	@Override
	public E get()
	{
		return value;
	}

	public void push()
	{
		pushedValues.add(value);
	}

	public E pop()
	{
		set(pushedValues.poll());
		return value;
	}

	public int size()
	{
		return pushedValues.size();
	}
}