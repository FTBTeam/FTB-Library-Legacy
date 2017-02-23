package com.feed_the_beast.ftbl.lib.block;

import com.feed_the_beast.ftbl.api.block.IBlockWithItem;
import com.feed_the_beast.ftbl.lib.tile.TileLM;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockLM extends Block implements IBlockWithItem
{
    //Model String = BlockStateSerializer.INSTANCE.get(getDefaultState())

    public BlockLM(String id, Material material, MapColor color)
    {
        super(material, color);
        setRegistryName(id);
        setUnlocalizedName(id.replace(':', '.'));
        setCreativeTab(CreativeTabs.MISC);
        setHardness(1.8F);
        setResistance(3F);
    }

    @Override
    public ItemBlock createItemBlock()
    {
        return new ItemBlockLM(this, false);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
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
                ((TileLM) te).onNeighborChange();
            }
            else if(te != null)
            {
                te.updateContainingBlockInfo();
            }
        }
    }
}