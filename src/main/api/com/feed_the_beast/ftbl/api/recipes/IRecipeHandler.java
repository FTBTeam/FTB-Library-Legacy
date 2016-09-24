package com.feed_the_beast.ftbl.api.recipes;

import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 23.08.2016.
 */
public interface IRecipeHandler
{
    ResourceLocation getID();

    boolean isActive();

    void loadRecipes(IRecipes recipes);
}
