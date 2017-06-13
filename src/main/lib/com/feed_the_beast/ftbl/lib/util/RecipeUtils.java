package com.feed_the_beast.ftbl.lib.util;

import com.feed_the_beast.ftbl.api.game.IRecipeIngredient;
import com.feed_the_beast.ftbl.lib.EnumDyeColorHelper;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.block.Block;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.function.Function;

public class RecipeUtils
{
	public static class LazyOreIngredient extends Ingredient
	{
		public final String oreName;
		private ItemStack[] ores = null;
		private IntList itemIds = null;

		public LazyOreIngredient(String s)
		{
			super(0);
			oreName = s;
		}

		@Override
		public ItemStack[] func_193365_a()
		{
			if (ores == null)
			{
				ores = OreDictionary.getOres(oreName, false).toArray(new ItemStack[0]);
				FTBLibFinals.LOGGER.info("Ores in recipe: " + OreDictionary.getOres(oreName));
			}

			return ores;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public IntList func_194139_b()
		{
			func_193365_a();
			if (itemIds == null || itemIds.size() != ores.length)
			{
				itemIds = new IntArrayList(ores.length);

				for (ItemStack itemstack : ores)
				{
					itemIds.add(RecipeItemHelper.func_194113_b(itemstack));
				}

				itemIds.sort(IntComparators.NATURAL_COMPARATOR);
			}

			return itemIds;
		}

		@Override
		public boolean apply(@Nullable ItemStack target)
		{
			if (target == null || func_193365_a().length == 0)
			{
				return false;
			}

			for (ItemStack ore : ores)
			{
				if (OreDictionary.itemMatches(target, ore, false))
				{
					return true;
				}
			}

			return false;
		}

		@Override
		public void invalidate()
		{
			ores = null;
			itemIds = null;
		}

		@Override
		public String toString()
		{
			return "'" + oreName + "'";
		}
	}

	private static final Function<Object, String> INGREDIENT_NAMES = o ->
	{
		if (o instanceof Ingredient && !(o instanceof LazyOreIngredient))
		{
			return StringUtils.toString(((Ingredient) o).func_193365_a(), null);
		}

		return null;
	};

	public static Ingredient getIngredient(@Nullable Object o)
	{
		if (o == null)
		{
			return Ingredient.field_193370_a;
		}
		if (o instanceof Ingredient)
		{
			return (Ingredient) o;
		}
		else if (o instanceof CharSequence)
		{
			return new LazyOreIngredient(o.toString());
		}
		else if (o instanceof ItemStack)
		{
			return Ingredient.func_193369_a((ItemStack) o);
		}
		else if (o instanceof Item)
		{
			return Ingredient.func_193367_a((Item) o);
		}
		else if (o instanceof Block)
		{
			return Ingredient.func_193367_a(Item.getItemFromBlock((Block) o));
		}
		else if (o instanceof ItemStack[])
		{
			return Ingredient.func_193369_a((ItemStack[]) o);
		}
		else if (o instanceof IRecipeIngredient)
		{
			return getIngredient(((IRecipeIngredient) o).getRecipeIngredient());
		}
		else if (o instanceof EnumDyeColor)
		{
			return getIngredient(EnumDyeColorHelper.get((EnumDyeColor) o).getDyeName());
		}
		return Ingredient.field_193370_a;
	}

	public static NonNullList<Ingredient> getIngredients(Object... in)
	{
		NonNullList<Ingredient> ingredients = NonNullList.create();

		for (int i = 0; i < in.length; i++)
		{
			ingredients.add(getIngredient(in[i]));
		}

		return ingredients;
	}

	public static void addIRecipe(IRecipe r)
	{
		CraftingManager.func_193372_a(new ResourceLocation(r.func_193358_e()), r);
	}

	public static void addRecipe(String group, ItemStack out, int width, Object... in)
	{
		if (!out.isEmpty())
		{
			NonNullList<Ingredient> ingredients = getIngredients(in);
			int height = MathHelper.ceil(ingredients.size() / (float) width);
			if (width * height != ingredients.size())
			{
				throw new IllegalArgumentException("Width x Height does not match recipe ingredient count! (" + width + " x " + height + ", " + ingredients.size() + ")");
			}

			for (Ingredient i : ingredients)
			{
				if (i instanceof LazyOreIngredient)
				{
					LazyOreIngredient l = (LazyOreIngredient) i;

					if (l.func_193365_a().length == 0)
					{
						FTBLibFinals.LOGGER.info("Skipped recipe for " + group + ", no ores for '" + l.oreName + "' found");
						return;
					}
				}
			}

			if (LMUtils.DEV_ENV)
			{
				FTBLibFinals.LOGGER.info("Added recipe for " + group + " with ingredients: " + StringUtils.toString(ingredients, INGREDIENT_NAMES));
			}

			addIRecipe(new ShapedRecipes(group, width, height, ingredients, out));
		}
	}

	public static void addShapelessRecipe(String group, ItemStack out, Object... in)
	{
		addIRecipe(new ShapelessRecipes(group, out, getIngredients(in)));
	}

	public static void addSmelting(ItemStack out, ItemStack in, float xp)
	{
		FurnaceRecipes.instance().addSmeltingRecipe(in, out, xp);
	}

	public static void addCircularRecipe(String group, ItemStack out, Object center, Object around)
	{
		Ingredient c = getIngredient(center);
		Ingredient a = getIngredient(around);
		addRecipe(group, out, 3, a, a, a, a, c, a, a, a, a);
	}

	public static void addCheckerboardRecipe(String group, ItemStack out, @Nullable Object center, Object inner, Object outer)
	{
		Ingredient i = getIngredient(inner);
		Ingredient o = getIngredient(outer);
		Ingredient c = getIngredient(center);
		addRecipe(group, out, 3, o, i, o, i, c, i, o, i, o);
	}
}