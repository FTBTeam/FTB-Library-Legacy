package com.feed_the_beast.ftblib.lib.item;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class FTBLibOreIngredient extends Ingredient
{
	public static class Factory implements IIngredientFactory
	{
		@Nonnull
		@Override
		public Ingredient parse(JsonContext context, JsonObject json)
		{
			return new FTBLibOreIngredient(net.minecraft.util.JsonUtils.getString(json, "ore"));
		}
	}

	private NonNullList<ItemStack> ores;
	private IntList itemIds = null;
	private ItemStack[] array = null;
	private int lastSizeA = -1, lastSizeL = -1;

	public FTBLibOreIngredient(String ore, @Nullable Map<ItemEntry, ItemStack> map)
	{
		super(0);

		if (ore.isEmpty())
		{
			ores = NonNullList.create();
		}
		else if (ore.endsWith("*"))
		{
			ores = NonNullList.create();

			if (map == null)
			{
				map = new LinkedHashMap<>();
			}

			ore = ore.substring(0, ore.length() - 1);

			for (String ore1 : OreDictionary.getOreNames())
			{
				if (ore1.startsWith(ore))
				{
					for (ItemStack stack : OreDictionary.getOres(ore1))
					{
						ItemEntry entry = ItemEntry.get(stack);

						if (!entry.isEmpty())
						{
							map.put(entry, stack);
						}
					}
				}
			}

			ores.addAll(map.values());
		}
		else
		{
			ores = OreDictionary.getOres(ore);
		}
	}

	public FTBLibOreIngredient(String ore)
	{
		this(ore, null);
	}

	@Override
	@Nonnull
	public ItemStack[] getMatchingStacks()
	{
		if (array == null || lastSizeA != ores.size())
		{
			NonNullList<ItemStack> lst = NonNullList.create();
			for (ItemStack itemstack : ores)
			{
				if (itemstack.getMetadata() == OreDictionary.WILDCARD_VALUE)
				{
					itemstack.getItem().getSubItems(CreativeTabs.SEARCH, lst);
				}
				else
				{
					lst.add(itemstack);
				}
			}
			array = lst.toArray(new ItemStack[lst.size()]);
			lastSizeA = ores.size();
		}
		return array;
	}


	@Override
	@Nonnull
	public IntList getValidItemStacksPacked()
	{
		if (itemIds == null || lastSizeL != ores.size())
		{
			itemIds = new IntArrayList(ores.size());

			for (ItemStack itemstack : ores)
			{
				if (itemstack.getMetadata() == OreDictionary.WILDCARD_VALUE)
				{
					NonNullList<ItemStack> lst = NonNullList.create();
					itemstack.getItem().getSubItems(CreativeTabs.SEARCH, lst);
					for (ItemStack item : lst)
					{
						itemIds.add(RecipeItemHelper.pack(item));
					}
				}
				else
				{
					itemIds.add(RecipeItemHelper.pack(itemstack));
				}
			}

			itemIds.sort(IntComparators.NATURAL_COMPARATOR);
			lastSizeL = ores.size();
		}

		return itemIds;
	}


	@Override
	public boolean apply(@Nullable ItemStack input)
	{
		if (input == null)
		{
			return false;
		}

		for (ItemStack target : ores)
		{
			if (OreDictionary.itemMatches(target, input, false))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	protected void invalidate()
	{
		itemIds = null;
		array = null;
	}
}