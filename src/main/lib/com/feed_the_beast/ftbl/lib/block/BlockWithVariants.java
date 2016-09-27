package com.feed_the_beast.ftbl.lib.block;

import com.feed_the_beast.ftbl.api.block.IBlockVariant;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 09.08.2016.
 */
public abstract class BlockWithVariants<T extends Enum<T> & IBlockVariant> extends BlockLM
{
    private BlockVariantLookup<T> metaLookup;

    public BlockWithVariants(Material m, CreativeTabs tab)
    {
        super(m);
        setCreativeTab(tab);
    }

    public abstract BlockVariantLookup<T> createMetaLookup();

    public BlockVariantLookup<T> getMetaLookup()
    {
        if(metaLookup == null)
        {
            metaLookup = createMetaLookup();
        }

        return metaLookup;
    }

    @Override
    public ItemBlock createItemBlock()
    {
        return new ItemBlockLM(this)
        {
            @Override
            @Nullable
            public String getVariantName(int meta)
            {
                return getMetaLookup().get(meta).getName();
            }
        };
    }

    @Override
    @Deprecated
    public Material getMaterial(IBlockState state)
    {
        return state.getValue(getMetaLookup().getProperty()).getMaterial();
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return state.getValue(getMetaLookup().getProperty()).getTileEntityClass() != null;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return state.getValue(getMetaLookup().getProperty()).createTileEntity(world);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
        for(T e : getMetaLookup().getValues())
        {
            list.add(e.getStack(1));
        }
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getValue(getMetaLookup().getProperty()).getMetadata();
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(getMetaLookup().getProperty(), getMetaLookup().get(meta));
    }

    @Override
    @Deprecated
    public MapColor getMapColor(IBlockState state)
    {
        return state.getValue(getMetaLookup().getProperty()).getMapColor();
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(getMetaLookup().getProperty()).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, getMetaLookup().getProperty());
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
    {
        return layer == state.getValue(getMetaLookup().getProperty()).getLayer();
    }

    @Override
    @Deprecated
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        if(canRenderInLayer(state, BlockRenderLayer.TRANSLUCENT))
        {
            IBlockState otherState = blockAccess.getBlockState(pos.offset(side));
            return state != otherState;
        }

        return super.shouldSideBeRendered(state, blockAccess, pos, side);
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return state.getValue(getMetaLookup().getProperty()).isOpaqueCube();
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state)
    {
        return state.getValue(getMetaLookup().getProperty()).isFullCube();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return state.getValue(getMetaLookup().getProperty()).onActivated(worldIn, pos, state, playerIn, hand, heldItem, side);
    }

    // Helpers //

    public void registerTileEntities()
    {
        for(T e : getMetaLookup())
        {
            if(e.getTileEntityClass() != null)
            {
                LMUtils.addTile(e.getTileEntityClass(), new ResourceLocation(getRegistryName().getResourceDomain(), e.getName()));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerModels()
    {
        Item item = Item.getItemFromBlock(this);

        for(T e : getMetaLookup())
        {
            ResourceLocation id = new ResourceLocation(getRegistryName().getResourceDomain(), "blocks/" + e.getName());
            ModelLoader.setCustomModelResourceLocation(item, e.getMetadata(), new ModelResourceLocation(id, "inventory"));
        }
    }
}