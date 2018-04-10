package com.feed_the_beast.ftblib.lib.util;

import com.feed_the_beast.ftblib.lib.OtherMods;
import com.google.common.base.Optional;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Made by LatvianModder
 */
public class CommonUtils
{
	public static final boolean DEV_ENV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
	public static final GameProfile FAKE_PLAYER_PROFILE = new GameProfile(StringUtils.fromString("069be1413c1b45c3b3b160d3f9fcd236"), "FakeForgePlayer");

	public static boolean isNEILoaded = false;
	public static File folderConfig, folderMinecraft, folderLocal;

	private static final Predicate<Object> PREDICATE_ALWAYS_TRUE = object -> true;
	public static final Object[] NO_OBJECTS = { };
	public static final JsonContext MINECRAFT_JSON_CONTEXT = new JsonContext("minecraft");

	public static final long TICKS_SECOND = 20L;
	public static final long TICKS_MINUTE = TICKS_SECOND * 60L;
	public static final long TICKS_HOUR = TICKS_MINUTE * 60L;

	public static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	public static final IBlockState AIR_STATE = Blocks.AIR.getDefaultState();

	@SuppressWarnings({"unchecked", "ConstantConditions"})
	public static <T> T cast(@Nullable Object o)
	{
		return (T) o;
	}

	public static <T> Predicate<T> alwaysTruePredicate()
	{
		return cast(PREDICATE_ALWAYS_TRUE);
	}

	@SafeVarargs
	@SuppressWarnings("varargs")
	public static <T> List<T> asList(@Nullable T... objects)
	{
		return objects == null || objects.length == 0 ? Collections.emptyList() : Arrays.asList(objects);
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

		isNEILoaded = Loader.isModLoaded(OtherMods.NEI);
	}

	public static String getNameFromState(IBlockState state)
	{
		if (state == AIR_STATE)
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

		if (state == AIR_STATE)
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
		return getStateFromName(name, CommonUtils.AIR_STATE);
	}

	public static void renameTag(NBTTagCompound nbt, String oldName, String newName)
	{
		NBTBase tag = nbt.getTag(oldName);

		if (tag != null)
		{
			nbt.removeTag(oldName);
			nbt.setTag(newName, tag);
		}
	}

	@Nullable
	public static NBTTagCompound nullIfEmpty(@Nullable NBTTagCompound nbt)
	{
		return nbt == null || nbt.hasNoTags() ? null : nbt;
	}

	@Nullable
	public static NBTTagList nullIfEmpty(@Nullable NBTTagList nbt)
	{
		return nbt == null || nbt.hasNoTags() ? null : nbt;
	}
}