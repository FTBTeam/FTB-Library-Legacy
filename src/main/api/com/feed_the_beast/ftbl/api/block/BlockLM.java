package com.feed_the_beast.ftbl.api.block;

import com.feed_the_beast.ftbl.api.tile.TileLM;
import com.latmod.lib.math.BlockStateSerializer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class BlockLM extends Block implements IBlockWithItem
{
    public BlockLM(Material m)
    {
        super(m);
        setCreativeTab(CreativeTabs.MISC);
        setHardness(1.8F);
        setResistance(3F);
    }

    @Override
    public ItemBlock createItemBlock()
    {
        return new ItemBlockLM(this);
    }

    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public String getUnlocalizedName()
    {
        return getRegistryName().getResourceDomain() + ".tile." + getRegistryName().getResourcePath();
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return super.hasTileEntity(state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
        list.add(new ItemStack(itemIn, 1, 0));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        if(hasTileEntity(state) && placer instanceof EntityPlayer)
        {
            TileEntity te = worldIn.getTileEntity(pos);

            if(te instanceof TileLM)
            {
                ((TileLM) te).onPlacedBy(placer, stack, state);
            }
        }
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
    {
        if(hasTileEntity(world.getBlockState(pos)))
        {
            TileEntity te = world.getTileEntity(pos);

            if(te instanceof TileLM && ((TileLM) te).isExplosionResistant())
            {
                return Float.MAX_VALUE;
            }
        }

        return super.getExplosionResistance(world, pos, exploder, explosion);
    }

    @Override
    @Deprecated
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        if(hasTileEntity(state))
        {
            TileEntity te = worldIn.getTileEntity(pos);

            if(te != null)
            {
                return te.receiveClientEvent(id, param);
            }
        }

        return false;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
    {
        if(hasTileEntity(world.getBlockState(pos)))
        {
            TileEntity te = world.getTileEntity(pos);

            if(te instanceof TileLM)
            {
                ((TileLM) te).onNeighborBlockChange(neighbor);
            }
        }
    }

    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return FULL_BLOCK_AABB;
    }

    @Override
    @Deprecated
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getStateFromMeta(meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.SOLID;
    }

    @SideOnly(Side.CLIENT)
    public final void registerDefaultModel()
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, BlockStateSerializer.INSTANCE.get(getDefaultState()));
    }
}