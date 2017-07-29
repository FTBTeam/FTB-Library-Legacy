package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.lib.NameMap;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class PropertyEnum<E> extends PropertyEnumAbstract<E>
{
	private final NameMap<E> nameMap;
	private E value;

	public static <T extends Enum<T>> PropertyEnum<T> create(NameMap<T> nm, Supplier<T> getter, Consumer<T> setter)
	{
		return new PropertyEnum<T>(nm)
		{
			@Override
			public T getValue()
			{
				return getter.get();
			}

			@Override
			public void setValue(T e)
			{
				setter.accept(e);
			}
		};
	}

	public PropertyEnum(NameMap<E> nm)
	{
		nameMap = nm;
		value = nm.defaultValue;
	}

	@Override
	public NameMap<E> getNameMap()
	{
		return nameMap;
	}

	@Override
	public E getValue()
	{
		return value;
	}

	@Override
	public void setValue(E e)
	{
		value = e;
	}
}