package com.feed_the_beast.ftbl.api.block;

import com.feed_the_beast.ftbl.api.item.IMaterial;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 06.09.2016.
 */
public interface IBlockVariant extends IMaterial
{
    @Nullable
    default Class<? extends TileEntity> getTileEntityClass()
    {
        return null;
    }

    @Nullable
    default TileEntity createTileEntity(World world)
    {
        return null;
    }

    default BlockRenderLayer getLayer()
    {
        return BlockRenderLayer.SOLID;
    }

    default MapColor getMapColor()
    {
        return MapColor.STONE;
    }

    default boolean isOpaqueCube()
    {
        return getLayer() == BlockRenderLayer.SOLID;
    }

    default boolean isFullCube()
    {
        return true;
    }

    Material getMaterial();

    default boolean onActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side)
    {
        return false;
    }
}
