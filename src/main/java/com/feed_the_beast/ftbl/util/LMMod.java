package com.feed_the_beast.ftbl.util;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.api.block.IBlockLM;
import com.feed_the_beast.ftbl.api.item.IItemLM;
import com.feed_the_beast.ftbl.api.recipes.LMRecipes;
import latmod.lib.util.FinalIDObject;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;

public class LMMod extends FinalIDObject
{
    public final String lowerCaseModID;
    public final List<IItemLM> itemsAndBlocks;
    private final String modAssets;
    public LMRecipes recipes;
    private ModContainer modContainer;

    public LMMod(String id)
    {
        super(id);
        lowerCaseModID = getID().toLowerCase();
        modAssets = lowerCaseModID + '.';
        itemsAndBlocks = new ArrayList<>();
        recipes = LMRecipes.defaultInstance;
    }

    public static LMMod create(String s)
    {
        LMMod mod = new LMMod(s);
        if(FTBLib.DEV_ENV)
        {
            FTBLibMod.logger.info("LMMod '" + mod.getID() + "' created");
        }
        return mod;
    }

    public ModContainer getModContainer()
    {
        if(modContainer == null)
        {
            modContainer = Loader.instance().getModObjectList().inverse().get(getID());
        }

        return modContainer;
    }

    public void setRecipes(LMRecipes r)
    {
        recipes = (r == null) ? new LMRecipes() : r;
    }

    public String getFullID()
    {
        return getID() + '-' + Loader.MC_VERSION + '-' + modContainer.getDisplayVersion();
    }

    public String getBlockName(String s)
    {
        return modAssets + "tile." + s;
    }

    public String getItemName(String s)
    {
        return modAssets + "item." + s;
    }

    public <K extends IForgeRegistryEntry<?>> K register(String s, K object)
    {
        object.setRegistryName(new ResourceLocation(getID(), s));
        GameRegistry.register(object);

        if(object instanceof IItemLM)
        {
            itemsAndBlocks.add((IItemLM) object);

            if(object instanceof IBlockLM)
            {
                ItemBlock ib = ((IBlockLM) object).createItemBlock();
                ib.setRegistryName(new ResourceLocation(getID(), s));
                GameRegistry.register(ib);
            }
        }

        return object;
    }

    public void onPostLoaded()
    {
        for(IItemLM i : itemsAndBlocks)
        {
            i.onPostLoaded();

            if(i instanceof IBlockLM)
            {
                ((IBlockLM) i).loadTiles();
            }

            FTBLibMod.proxy.loadModels(i);
        }
    }

    public void loadRecipes()
    {
        for(IItemLM i : itemsAndBlocks)
        {
            i.loadRecipes();
        }

        if(recipes != null)
        {
            recipes.loadRecipes();
        }
    }
}