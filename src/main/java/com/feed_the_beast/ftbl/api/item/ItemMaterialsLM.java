package com.feed_the_beast.ftbl.api.item;

import com.latmod.lib.util.LMUtils;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class ItemMaterialsLM extends Item
{
    public final TIntObjectHashMap<IMaterial> materials;

    public ItemMaterialsLM()
    {
        materials = new TIntObjectHashMap<>();
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Nonnull
    public String getFolder()
    {
        return "";
    }

    public final void add(IMaterial m)
    {
        if(m.isAdded())
        {
            m.setItem(this);
        }

        materials.put(m.getMetadata(), m);
    }

    public final void addAll(Class<?> c)
    {
        try
        {
            LMUtils.getObjects(IMaterial.class, c, null).forEach(this::add);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Nonnull
    @Override
    public String getUnlocalizedName(ItemStack is)
    {
        IMaterial m = materials.get(is.getMetadata());

        if(m != null)
        {
            String s = getFolder();
            return getRegistryName().getResourceDomain() + ".item." + (s.isEmpty() ? m.getName() : (s + '.' + m.getName()));
        }

        return "item.null";
    }

    @SideOnly(Side.CLIENT)
    public void loadModels()
    {
        for(IMaterial i : materials.valueCollection())
        {
            String s = getFolder();
            ResourceLocation rl = new ResourceLocation(getRegistryName().getResourceDomain(), s.isEmpty() ? i.getName() : (s + '/' + i.getName()));
            ModelLoader.setCustomModelResourceLocation(this, i.getMetadata(), new ModelResourceLocation(rl, "inventory"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull Item item, CreativeTabs c, List<ItemStack> l)
    {
        for(IMaterial m : materials.valueCollection())
        {
            if(m.isAdded())
            {
                l.add(new ItemStack(m.getItem(), 1, m.getMetadata()));
            }
        }
    }
}
