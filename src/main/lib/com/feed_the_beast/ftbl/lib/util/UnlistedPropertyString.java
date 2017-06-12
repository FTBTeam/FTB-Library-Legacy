package com.feed_the_beast.ftbl.lib.util;

import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.function.Predicate;

/**
 * @author LatvianModder
 */
public class UnlistedPropertyString implements IUnlistedProperty<String>
{
	public static UnlistedPropertyString create(String name, Predicate<String> validator)
	{
		return new UnlistedPropertyString(name, validator);
	}

	public static UnlistedPropertyString create(String name)
	{
		return create(name, LMUtils.alwaysTruePredicate());
	}

	private final String name;
	private final Predicate<String> validator;

	private UnlistedPropertyString(String n, Predicate<String> v)
	{
		name = n;
		validator = v;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public boolean isValid(String value)
	{
		return validator.test(value);
	}

	@Override
	public Class<String> getType()
	{
		return String.class;
	}

	@Override
	public String valueToString(String value)
	{
		return value;
	}
}
