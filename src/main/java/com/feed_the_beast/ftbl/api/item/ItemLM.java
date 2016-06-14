package com.feed_the_beast.ftbl.api.item;

import com.feed_the_beast.ftbl.util.LMMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class ItemLM extends Item implements IItemLM
{
    public abstract LMMod getMod();

    @Override
    public final Item getItem()
    {
        return this;
    }

    @Nonnull
    @Override
    public final String getID()
    {
        return getRegistryName().toString();
    }

    @Override
    public void onPostLoaded()
    {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void loadModels()
    {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public void loadRecipes()
    {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull Item item, CreativeTabs c, List<ItemStack> l)
    {
        l.add(new ItemStack(item, 1, 0));
    }

    @Nonnull
    @Override
    public String getUnlocalizedName(ItemStack is)
    {
        return getMod().getItemName(getRegistryName().getResourcePath());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack is, EntityPlayer ep, List<String> l, boolean b)
    {
    }
}