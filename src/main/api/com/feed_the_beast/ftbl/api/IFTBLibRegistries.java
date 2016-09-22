package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.gui.IGuiHandler;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.feed_the_beast.ftbl.api.recipes.IRecipeHandler;
import com.latmod.lib.reg.SimpleRegistry;
import com.latmod.lib.reg.SyncedRegistry;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public interface IFTBLibRegistries
{
    SimpleRegistry<ResourceLocation, ISyncData> syncedData();

    SyncedRegistry<ResourceLocation, IGuiHandler> guis();

    SimpleRegistry<ResourceLocation, ISidebarButton> sidebarButtons();

    SimpleRegistry<ResourceLocation, IRecipeHandler> recipeHandlers();

    Collection<ITickable> tickables();
}