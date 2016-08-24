package com.feed_the_beast.ftbl.api.item;

import com.feed_the_beast.ftbl.util.IMetaLookup;
import com.latmod.lib.util.LMUtils;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public class ItemMaterialsLM extends Item implements IMetaLookup<IMaterial>
{
    public enum Air implements IMaterial
    {
        INSTANCE;

        @Override
        public Item getItem()
        {
            return Item.getItemFromBlock(Blocks.AIR);
        }

        @Override
        public int getMetadata()
        {
            return 0;
        }

        @Override
        public String getName()
        {
            return "air";
        }

        @Override
        public boolean isAdded()
        {
            return false;
        }
    }

    private final TIntObjectHashMap<IMaterial> materials;
    private final TIntObjectHashMap<String> unlocalizedNameMap;
    private IMaterial defValue;
    private String folder = "";

    public ItemMaterialsLM()
    {
        materials = new TIntObjectHashMap<>();
        defValue = Air.INSTANCE;
        unlocalizedNameMap = new TIntObjectHashMap<>();
        setHasSubtypes(true);
        setMaxDamage(0);
    }

    public void setFolder(@Nonnull String s)
    {
        folder = s;
    }

    public void setDefaultMaterial(@Nonnull IMaterial m)
    {
        defValue = m;
    }

    public void add(@Nonnull IMaterial m)
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
            addAll(LMUtils.getObjects(IMaterial.class, c, null));
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public final void addAll(Iterable<IMaterial> c)
    {
        c.forEach(this::add);
    }

    @Override
    @Nonnull
    public Collection<IMaterial> getValues()
    {
        return materials.valueCollection();
    }

    @Override
    @Nonnull
    public IMaterial get(int metadata)
    {
        IMaterial m = materials.get(metadata);
        return (m == null) ? defValue : m;
    }

    @Nonnull
    @Override
    public String getUnlocalizedName(ItemStack is)
    {
        int meta = is.getMetadata();

        String s = unlocalizedNameMap.get(meta);

        if(s == null)
        {
            IMaterial m = get(meta);

            if(m.isAdded())
            {
                s = getRegistryName().getResourceDomain() + ".item." + (folder.isEmpty() ? m.getName() : (folder + '.' + m.getName()));
            }
            else
            {
                s = m.getStack(1).getUnlocalizedName();
            }

            unlocalizedNameMap.put(meta, s);
        }

        return s;
    }

    @SideOnly(Side.CLIENT)
    public void loadModels()
    {
        for(IMaterial i : getValues())
        {
            ResourceLocation rl = new ResourceLocation(getRegistryName().getResourceDomain(), folder.isEmpty() ? i.getName() : (folder + '/' + i.getName()));
            ModelLoader.setCustomModelResourceLocation(this, i.getMetadata(), new ModelResourceLocation(rl, "inventory"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull Item item, CreativeTabs c, List<ItemStack> l)
    {
        for(IMaterial m : getValues())
        {
            if(m.isAdded())
            {
                l.add(new ItemStack(m.getItem(), 1, m.getMetadata()));
            }
        }
    }
}
