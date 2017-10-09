package com.feed_the_beast.ftbl.lib.util.misc;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
	private final List<E> pushedValues;

	public Pushable(E def, Consumer<E> _set)
	{
		defaultValue = def;
		value = def;
		consumer = _set;
		pushedValues = new ArrayList<>(3);
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
		Preconditions.checkState(pushedValues.size() > 0);
		set(pushedValues.remove(pushedValues.size() - 1));
		return value;
	}

	public int size()
	{
		return pushedValues.size();
	}
}