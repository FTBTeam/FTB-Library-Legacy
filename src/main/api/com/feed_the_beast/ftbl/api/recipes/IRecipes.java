package com.feed_the_beast.ftbl.api.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

/**
 * Created by LatvianModder on 23.08.2016.
 */
public interface IRecipes
{
    IRecipe addIRecipe(IRecipe r);

    IRecipe addRecipe(ItemStack out, Object... in);

    IRecipe addShapelessRecipe(ItemStack out, Object... in);

    void addSmelting(ItemStack out, ItemStack in, float xp);
}