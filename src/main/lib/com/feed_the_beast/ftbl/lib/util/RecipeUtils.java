package com.feed_the_beast.ftbl.lib.util;

import com.feed_the_beast.ftbl.lib.EnumDyeColorHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nullable;

public class RecipeUtils
{
	public static Object fixObject(@Nullable Object o)
	{
		if (o == null)
		{
			return ItemStack.EMPTY;
		}
		else if (o instanceof EnumDyeColor)
		{
			return EnumDyeColorHelper.get((EnumDyeColor) o).getDyeName();
		}

		return o;
	}

	public static Object[] fixObjects(Object[] in)
	{
		for (int i = 0; i < in.length; i++)
		{
			in[i] = fixObject(in[i]);
		}

		return in;
	}

	public static void addIRecipe(ResourceLocation group, IRecipe r)
	{
		CraftingManager.func_193372_a(group, r);
	}

	public static void addRecipe(ResourceLocation group, ItemStack out, Object... in)
	{
		if (!out.isEmpty())
		{
			addIRecipe(group, new ShapedOreRecipe(group, out, fixObjects(in)));
		}
	}

	public static void addShapelessRecipe(ResourceLocation group, ItemStack out, Object... in)
	{
		addIRecipe(group, new ShapelessOreRecipe(group, out, fixObjects(in)));
	}

	public static void addSmelting(ItemStack out, ItemStack in, float xp)
	{
		FurnaceRecipes.instance().addSmeltingRecipe(in, out, xp);
	}

	public static void addCircularRecipe(ResourceLocation group, ItemStack out, Object center, Object around)
	{
		addRecipe(group, out, "AAA", "ACA", "AAA", 'C', center, 'A', around);
	}

	public static void addCheckerboardRecipe(ResourceLocation group, ItemStack out, @Nullable Object center, Object inner, Object outer)
	{
		if (center == null)
		{
			addRecipe(group, out, "OIO", "ICI", "OIO", 'I', inner, 'O', outer);
		}
		else
		{
			addRecipe(group, out, "OIO", "ICI", "OIO", 'C', center, 'I', inner, 'O', outer);
		}
	}
}