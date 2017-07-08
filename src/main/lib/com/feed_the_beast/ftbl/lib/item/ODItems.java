package com.feed_the_beast.ftbl.lib.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ODItems
{
	public static Set<String> getOreNames(@Nullable Set<String> l, ItemStack is)
	{
		if (is.isEmpty())
		{
			return l == null ? Collections.emptySet() : l;
		}

		int[] ai = OreDictionary.getOreIDs(is);

		if (ai.length > 0)
		{
			if (l == null)
			{
				l = new HashSet<>(ai.length);
			}

			for (int i : ai)
			{
				l.add(OreDictionary.getOreName(i));
			}

			return l;
		}

		return Collections.emptySet();
	}

	public static boolean itemHasOre(ItemStack is, String s)
	{
		int[] ai = OreDictionary.getOreIDs(is);

		if (ai.length > 0)
		{
			for (int i : ai)
			{
				if (s.equals(OreDictionary.getOreName(i)))
				{
					return true;
				}
			}
		}

		return false;
	}
}