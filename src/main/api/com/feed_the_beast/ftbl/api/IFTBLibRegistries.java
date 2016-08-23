package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.config.ConfigFile;
import com.feed_the_beast.ftbl.api.gui.IGuiHandler;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.feed_the_beast.ftbl.api.recipes.IRecipeHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public interface IFTBLibRegistries
{
    IRegistry<ResourceLocation, INBTSerializable<NBTTagCompound>> syncedData();

    ISyncedRegistry<IGuiHandler> guis();

    IRegistry<ResourceLocation, ISidebarButton> sidebarButtons();

    IRegistry<String, ConfigFile> configFiles();

    IIntIDRegistry notifications();

    IRegistry<ResourceLocation, IRecipeHandler> recipeHandlers();
}