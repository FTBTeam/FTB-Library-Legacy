package com.feed_the_beast.ftbl.lib.util;

import com.feed_the_beast.ftbl.client.FTBLibClientConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Made by LatvianModder
 */
public class CommonUtils
{
	public static final boolean DEV_ENV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
	public static final Logger DEV_LOGGER = LogManager.getLogger("FTBLibDev");

	public static boolean userIsLatvianModder = false, isNEILoaded = false;
	public static File folderConfig, folderMinecraft, folderModpack, folderLocal, folderWorld;

	public static final Comparator<Package> PACKAGE_COMPARATOR = (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName());
	private static final Predicate<Object> PREDICATE_ALWAYS_TRUE = object -> true;
	public static final Object[] NO_OBJECTS = { };

	public static <T> T cast(Object o)
	{
		return (T) o;
	}

	public static <T> Predicate<T> alwaysTruePredicate()
	{
		return (Predicate<T>) PREDICATE_ALWAYS_TRUE;
	}

	public static boolean isNEILoaded()
	{
		return isNEILoaded && !FTBLibClientConfig.IGNORE_NEI.getBoolean();
	}

	public static void init(File configFolder)
	{
		folderConfig = configFolder;
		folderMinecraft = folderConfig.getParentFile();
		folderModpack = new File(folderMinecraft, "modpack/");
		folderLocal = new File(folderMinecraft, "local/");

		if (!folderModpack.exists())
		{
			folderModpack.mkdirs();
		}
		if (!folderLocal.exists())
		{
			folderLocal.mkdirs();
		}

		isNEILoaded = Loader.isModLoaded("NotEnoughItems") || Loader.isModLoaded("nei") || Loader.isModLoaded("notenoughitems");
	}

	public static <E> Map<String, E> getObjects(@Nullable Class<E> type, Class<?> fields, @Nullable Object obj, boolean immutable)
	{
		Map<String, E> map = new HashMap<>();

		for (Field f : fields.getDeclaredFields())
		{
			f.setAccessible(true);

			if (type == null || type.isAssignableFrom(f.getType()))
			{
				try
				{
					map.put(f.getName(), (E) f.get(obj));
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}

		return immutable ? Collections.unmodifiableMap(map) : map;
	}

	@Nullable
	public static URL get(String url)
	{
		try
		{
			return new URL(url);
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	public static String getNameFromState(IBlockState state)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(Block.REGISTRY.getNameForObject(state.getBlock()));

		if (!state.getProperties().isEmpty())
		{
			builder.append('[');
			final boolean[] first = {true};

			state.getProperties().forEach((iProperty, comparable) ->
			{
				if (first[0])
				{
					first[0] = false;
				}
				else
				{
					builder.append(',');
				}
				builder.append(iProperty.getName());
				builder.append('=');
				builder.append(iProperty.getName(cast(comparable)));
			});

			builder.append(']');
		}

		return builder.toString();
	}

	public static IBlockState getStateFromName(String name)
	{
		int p = name.indexOf('[');
		String stateName = p == -1 ? name : name.substring(0, p - 1);
		Block block = Block.REGISTRY.getObject(new ResourceLocation(stateName));

		if (p >= 0)
		{
			for (String property : name.substring(p, name.length() - 1).split(","))
			{

			}
		}

		return block.getDefaultState();
	}
}