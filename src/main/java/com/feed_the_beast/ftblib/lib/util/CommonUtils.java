package com.feed_the_beast.ftblib.lib.util;

import com.feed_the_beast.ftblib.lib.block.BlockFlags;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Made by LatvianModder
 */
public class CommonUtils
{
	public static final boolean DEV_ENV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
	public static final GameProfile FAKE_PLAYER_PROFILE = new GameProfile(StringUtils.fromString("069be1413c1b45c3b3b160d3f9fcd236"), "FakeForgePlayer");
	private static ListMultimap<String, ModContainer> packageOwners = null;

	public static File folderConfig, folderMinecraft, folderLocal;

	private static final Predicate<Object> PREDICATE_ALWAYS_TRUE = object -> true;
	public static final Object[] NO_OBJECTS = { };

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

	public static void init(File configFolder)
	{
		folderConfig = configFolder;
		folderMinecraft = folderConfig.getParentFile();
		folderLocal = new File(folderMinecraft, "local/");

		if (!folderLocal.exists())
		{
			folderLocal.mkdirs();
		}
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
					com.google.common.base.Optional<?> propValue = property1.parseValue(p1[1]);

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

	public static IBlockState notifyBlockUpdate(World world, BlockPos pos, @Nullable IBlockState state)
	{
		if (state == null)
		{
			state = world.getBlockState(pos);
		}

		world.notifyBlockUpdate(pos, state, state, BlockFlags.DEFAULT_AND_RERENDER);
		return state;
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