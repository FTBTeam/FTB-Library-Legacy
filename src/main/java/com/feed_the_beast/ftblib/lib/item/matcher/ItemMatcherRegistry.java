package com.feed_the_beast.ftblib.lib.item.matcher;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class ItemMatcherRegistry
{
	private static class MatcherEntry
	{
		private Predicate<NBTBase> predicate;
		private Supplier<IItemMatcher> supplier;
	}

	private static final ArrayList<MatcherEntry> ENTRIES = new ArrayList<>();

	public static void register(Predicate<NBTBase> predicate, Supplier<IItemMatcher> supplier)
	{
		MatcherEntry entry = new MatcherEntry();
		entry.predicate = predicate;
		entry.supplier = supplier;
		ENTRIES.add(entry);
	}

	static
	{
		register(nbt -> nbt instanceof NBTTagCompound && ((NBTTagCompound) nbt).hasKey("ore"), OreNameMatcher::new);
		register(nbt -> nbt instanceof NBTTagCompound && ((NBTTagCompound) nbt).hasKey("and"), AndMatcher::new);
		register(nbt -> nbt instanceof NBTTagCompound && ((NBTTagCompound) nbt).hasKey("or"), OrMatcher::new);
	}

	public static IItemMatcher createMatcher(@Nullable NBTBase nbt)
	{
		if (nbt == null || nbt.isEmpty())
		{
			return NoItemMatcher.INSTANCE;
		}

		for (MatcherEntry entry : ENTRIES)
		{
			if (entry.predicate.test(nbt))
			{
				IItemMatcher matcher = entry.supplier.get();
				matcher.fromNBT(nbt);

				if (matcher.isValid())
				{
					return matcher;
				}
			}
		}

		ItemStackMatcher matcher = new ItemStackMatcher();
		matcher.fromNBT(nbt);
		return matcher.isValid() ? matcher : NoItemMatcher.INSTANCE;
	}
}