package com.feed_the_beast.ftbl.lib.block;

import com.feed_the_beast.ftbl.api.block.IBlockWithItem;
import com.feed_the_beast.ftbl.lib.math.BlockStateSerializer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockLM extends Block implements IBlockWithItem
{
    public BlockLM(Material m, ResourceLocation id)
    {
        super(m);
        setRegistryName(id);
        setUnlocalizedName(id.toString().replace(':', '.'));
        setCreativeTab(CreativeTabs.MISC);
        setHardness(1.8F);
        setResistance(3F);
    }

    @Override
    public ItemBlock createItemBlock()
    {
        return new ItemBlockLM(this, true);
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

            if(te != null)
            {
                te.updateContainingBlockInfo();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public final void registerDefaultModel()
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, BlockStateSerializer.INSTANCE.get(getDefaultState()));
    }
}