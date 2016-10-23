package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.api.IRecipes;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by LatvianModder on 23.10.2016.
 */
public class RegisterRecipesEvent extends Event
{
    private final IRecipes recipes;

    public RegisterRecipesEvent(IRecipes r)
    {
        recipes = r;
    }

    public IRecipes getRecipes()
    {
        return recipes;
    }
}