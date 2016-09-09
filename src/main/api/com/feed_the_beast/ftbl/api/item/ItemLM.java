package com.feed_the_beast.ftbl.api.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;

public abstract class ItemLM extends Item
{
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return getRegistryName().getResourceDomain() + ".item." + getRegistryName().getResourcePath();
    }

    public final void registerDefaultModel()
    {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}