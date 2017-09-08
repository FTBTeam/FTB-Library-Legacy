package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.lib.NameMap;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class ConfigEnum<E> extends ConfigEnumAbstract<E>
{
	private final NameMap<E> nameMap;
	private E value;

	public static <T extends Enum<T>> ConfigEnum<T> create(NameMap<T> nm, Supplier<T> getter, Consumer<T> setter)
	{
		return new ConfigEnum<T>(nm)
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

	public ConfigEnum(NameMap<E> nm)
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