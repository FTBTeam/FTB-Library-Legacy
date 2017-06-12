package com.feed_the_beast.ftbl.lib.util;

import com.feed_the_beast.ftbl.api.block.IBlockWithItem;
import com.feed_the_beast.ftbl.client.FTBLibClientConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
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
public class LMUtils
{
	public static final boolean DEV_ENV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
	public static final Logger DEV_LOGGER = LogManager.getLogger("FTBLibDev");

	public static boolean userIsLatvianModder = false, isNEILoaded = false;
	public static File folderConfig, folderMinecraft, folderModpack, folderLocal, folderWorld;

	public static final Comparator<Package> PACKAGE_COMPARATOR = (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName());
	private static final Predicate<Object> PREDICATE_ALWAYS_TRUE = object -> true;

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

	public static <K extends IForgeRegistryEntry<?>> void register(K object)
	{
		GameRegistry.register(object);

		if (object instanceof IBlockWithItem)
		{
			ItemBlock ib = ((IBlockWithItem) object).createItemBlock();
			ib.setRegistryName(object.getRegistryName());
			GameRegistry.register(ib);
		}
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

	public static <E extends IForgeRegistryEntry<E>> Map<String, E> findAndRegister(Class<E> type, String modID, Class<?> fields, @Nullable Object obj)
	{
		Map<String, E> map = new HashMap<>();

		for (Field f : fields.getDeclaredFields())
		{
			f.setAccessible(true);

			if (type.isAssignableFrom(f.getType()))
			{
				try
				{
					E o = (E) f.get(obj);
					o.setRegistryName(new ResourceLocation(modID, f.getName().toLowerCase()));
					GameRegistry.register(o);
					map.put(f.getName().toLowerCase(), o);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}

		return Collections.unmodifiableMap(map);
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

	public static String getName(IBlockState state)
	{
		ResourceLocation id = state.getBlock().getRegistryName();
		return id.getResourceDomain() + '_' + id.getResourcePath().replace('.', '_') + '_' + state.getBlock().getMetaFromState(state);
	}
}