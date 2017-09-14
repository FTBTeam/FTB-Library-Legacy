package com.feed_the_beast.ftbl.lib.util;

import com.google.common.base.Optional;
import com.google.gson.JsonElement;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Made by LatvianModder
 */
public class CommonUtils
{
	public static final boolean DEV_ENV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
	public static final Logger DEV_LOGGER = LogManager.getLogger("FTBLibDev");

	public static boolean isNEILoaded = false;
	public static File folderConfig, folderMinecraft, folderLocal, folderWorld;

	public static final Comparator<Package> PACKAGE_COMPARATOR = (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName());
	private static final Predicate<Object> PREDICATE_ALWAYS_TRUE = object -> true;
	public static final Object[] NO_OBJECTS = { };
	public static final JsonContext MINECRAFT_JSON_CONTEXT = new JsonContext("minecraft");

	public static final long TICKS_SECOND = 20L;
	public static final long TICKS_MINUTE = TICKS_SECOND * 60L;
	public static final long TICKS_HOUR = TICKS_MINUTE * 60L;

	public static <T> T cast(@Nullable Object o)
	{
		return (T) o;
	}

	public static <T> Predicate<T> alwaysTruePredicate()
	{
		return cast(PREDICATE_ALWAYS_TRUE);
	}

	public static boolean isNEILoaded()
	{
		return isNEILoaded;
	}

	public static void init(File configFolder)
	{
		folderConfig = configFolder;
		folderMinecraft = folderConfig.getParentFile();
		folderLocal = new File(folderMinecraft, "local/");

		if (!folderLocal.exists())
		{
			folderLocal.mkdirs();
		}

		isNEILoaded = Loader.isModLoaded("nei") || Loader.isModLoaded("notenoughitems");
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

	public static String getNameFromState(IBlockState state)
	{
		if (state == Blocks.AIR.getDefaultState())
		{
			return "minecraft:air";
		}


		StringBuilder builder = new StringBuilder();
		builder.append(Block.REGISTRY.getNameForObject(state.getBlock()));

		if (state != state.getBlock().getDefaultState() && !state.getProperties().isEmpty())
		{
			builder.append('[');
			boolean first = true;

			for (Map.Entry<IProperty<?>, Comparable<?>> entry : state.getProperties().entrySet())
			{
				if (first)
				{
					first = false;
				}
				else
				{
					builder.append(',');
				}

				builder.append(entry.getKey().getName());
				builder.append('=');
				builder.append(entry.getKey().getName(cast(entry.getValue())));
			}

			builder.append(']');
		}
		return builder.toString();
	}

	public static IBlockState getStateFromName(String name, IBlockState def)
	{
		if (name.isEmpty())
		{
			return def;
		}

		int p = name.indexOf('[');
		String stateName = p == -1 ? name : name.substring(0, p);
		IBlockState state = Block.REGISTRY.getObject(new ResourceLocation(stateName)).getDefaultState();

		if (state == Blocks.AIR.getDefaultState())
		{
			return def;
		}

		if (p >= 0)
		{
			for (String property : name.substring(p + 1, name.length() - 1).split(","))
			{
				String[] p1 = property.split("=", 2);
				IProperty<?> property1 = state.getBlock().getBlockState().getProperty(p1[0]);

				if (property1 != null)
				{
					Optional<?> propValue = property1.parseValue(p1[1]);

					if (propValue.isPresent())
					{
						state = state.withProperty(property1, cast(propValue.get()));
					}
				}
			}
		}

		return state;
	}

	public static IBlockState getStateFromName(String name)
	{
		return getStateFromName(name, Blocks.AIR.getDefaultState());
	}

	public static boolean isOneOf(Object original, Object... objects)
	{
		for (Object o : objects)
		{
			if (Objects.equals(original, o))
			{
				return true;
			}
		}

		return false;
	}

	public static Ingredient getIngredient(@Nullable JsonElement element)
	{
		if (element == null || element.isJsonNull() || element.isJsonArray() && element.getAsJsonArray().size() == 0)
		{
			return Ingredient.EMPTY;
		}

		return CraftingHelper.getIngredient(element, MINECRAFT_JSON_CONTEXT);
	}
}