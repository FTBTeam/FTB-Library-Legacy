package com.feed_the_beast.ftblib.lib.util;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * Made by LatvianModder
 */
public class CommonUtils
{
	private static ListMultimap<String, ModContainer> packageOwners = null;
	private static final Predicate<Object> PREDICATE_ALWAYS_TRUE = object -> true;

	@SuppressWarnings({"unchecked", "ConstantConditions"})
	public static <T> T cast(@Nullable Object o)
	{
		return (T) o;
	}

	public static <T> Predicate<T> alwaysTruePredicate()
	{
		return cast(PREDICATE_ALWAYS_TRUE);
	}

	@Nullable
	public static ModContainer getModContainerForClass(Class clazz)
	{
		if (packageOwners == null)
		{
			try
			{
				LoadController instance = ReflectionHelper.getPrivateValue(Loader.class, Loader.instance(), "modController");
				packageOwners = ReflectionHelper.getPrivateValue(LoadController.class, instance, "packageOwners");
			}
			catch (Exception ex)
			{
				packageOwners = ImmutableListMultimap.of();
			}
		}

		if (packageOwners.isEmpty())
		{
			return null;
		}

		String pkg = clazz.getName().substring(0, clazz.getName().lastIndexOf('.'));
		return packageOwners.containsKey(pkg) ? packageOwners.get(pkg).get(0) : null;
	}
}