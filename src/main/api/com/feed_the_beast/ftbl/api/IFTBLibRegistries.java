package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.config.ConfigFile;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.feed_the_beast.ftbl.api.gui.IGuiHandler;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.feed_the_beast.ftbl.api.recipes.IRecipeHandler;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public interface IFTBLibRegistries
{
    IRegistry<ResourceLocation, ISyncData> syncedData();

    ISyncedRegistry<IGuiHandler> guis();

    IRegistry<ResourceLocation, ISidebarButton> sidebarButtons();

    IRegistry<String, ConfigFile> configFiles();

    IIntIDRegistry notifications();

    IRegistry<ResourceLocation, IRecipeHandler> recipeHandlers();

    Collection<ITickable> tickables();

    ISyncedRegistry<IConfigValueProvider> configValues();
}