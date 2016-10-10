package com.feed_the_beast.ftbl.api.recipes;

import com.feed_the_beast.ftbl.api.IRegistryObject;

/**
 * Created by LatvianModder on 23.08.2016.
 */
public interface IRecipeHandler extends IRegistryObject
{
    void loadRecipes(IRecipes recipes);
}
