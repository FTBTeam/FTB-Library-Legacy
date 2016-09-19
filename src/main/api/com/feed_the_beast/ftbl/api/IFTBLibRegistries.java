package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.config.IConfigFile;
import com.feed_the_beast.ftbl.api.gui.IGuiHandler;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.feed_the_beast.ftbl.api.recipes.IRecipeHandler;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public interface IFTBLibRegistries
{
    IRegistry<ResourceLocation, ISyncData> syncedData();

    ISyncedRegistry<ResourceLocation, IGuiHandler> guis();

    IRegistry<ResourceLocation, ISidebarButton> sidebarButtons();

    void registerConfigFile(String id, IConfigFile file, @Nonnull ITextComponent displayName);

    IIntIDRegistry<ResourceLocation> notifications();

    IRegistry<ResourceLocation, IRecipeHandler> recipeHandlers();

    Collection<ITickable> tickables();
}