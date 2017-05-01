package com.feed_the_beast.ftbl.lib.util;

import com.feed_the_beast.ftbl.lib.EnumDyeColorHelper;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nullable;

public class RecipeUtils
{
    public static Object fixObject(@Nullable Object o)
    {
        if(o == null)
        {
            return ItemStackTools.getEmptyStack();
        }
        else if(o instanceof EnumDyeColor)
        {
            return EnumDyeColorHelper.get((EnumDyeColor) o).getDyeName();
        }

        return o;
    }

    public static Object[] fixObjects(Object[] in)
    {
        for(int i = 0; i < in.length; i++)
        {
            in[i] = fixObject(in[i]);
        }

        return in;
    }

    public static void addIRecipe(IRecipe r)
    {
        CraftingManager.getInstance().getRecipeList().add(r);
    }

    public static void addRecipe(ItemStack out, Object... in)
    {
        if(!ItemStackTools.isEmpty(out))
        {
            addIRecipe(new ShapedOreRecipe(out, fixObjects(in)));
        }
    }

    public static void addShapelessRecipe(ItemStack out, Object... in)
    {
        addIRecipe(new ShapelessOreRecipe(out, fixObjects(in)));
    }

    public static void addSmelting(ItemStack out, ItemStack in, float xp)
    {
        FurnaceRecipes.instance().addSmeltingRecipe(in, out, xp);
    }

    public static void addCircularRecipe(ItemStack out, Object center, Object around)
    {
        GameRegistry.addRecipe(new ShapedOreRecipe(out, "AAA", "ACA", "AAA", 'C', fixObject(center), 'A', fixObject(around)));
    }
}