package com.feed_the_beast.ftbl.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class Pushable<E> implements Supplier<E>
{
	public final E defaultValue;
	private final Consumer<E> consumer;

	private E value;
	private final List<E> pushedValues;

	public Pushable(E def, Consumer<E> _set)
	{
		defaultValue = def;
		consumer = _set;
		pushedValues = new ArrayList<>(3);
		consumer.accept(defaultValue);
	}

	public final void set(E val)
	{
		if (!Objects.equals(value, val))
		{
			value = val;
			onSet();
		}
	}

	public final void reset()
	{
		set(defaultValue);
	}

	protected void onSet()
	{
		consumer.accept(get());
	}

	@Override
	public final E get()
	{
		return value;
	}

	public final void push()
	{
		pushedValues.add(value);
	}

	public final void pop()
	{
		set(pushedValues.remove(pushedValues.size() - 1));
	}
}