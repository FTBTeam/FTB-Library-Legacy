package com.feed_the_beast.ftblib.lib.block;

import com.feed_the_beast.ftblib.lib.tile.TileBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class BlockBase extends Block
{
	public BlockBase(String mod, String id, Material material, MapColor color)
	{
		super(material, color);
		setRegistryName(mod, id);
		setUnlocalizedName(mod + '.' + id);
		setHardness(1.8F);
		setCreativeTab(CreativeTabs.MISC);
	}

	public boolean dropSpecial(IBlockState state)
	{
		return false;
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		if (dropSpecial(state))
		{
			TileEntity tileEntity = world.getTileEntity(pos);

			if (tileEntity instanceof TileBase)
			{
				((TileBase) tileEntity).destroyedByCreativePlayer = player.capabilities.isCreativeMode;
			}
		}
	}

	@Override
	public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, float chance, int fortune)
	{
		if (!dropSpecial(state))
		{
			super.dropBlockAsItemWithChance(world, pos, state, chance, fortune);
		}
	}

	@Override
	@Deprecated
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return isNormalCube(state, world, pos) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		if (dropSpecial(state))
		{
			TileEntity tileEntity = world.getTileEntity(pos);

			if (tileEntity instanceof TileBase)
			{
				TileBase tile = (TileBase) tileEntity;

				if (!tile.destroyedByCreativePlayer || tile.shouldDrop())
				{
					spawnAsEntity(world, pos, createStack(state, tileEntity));
				}

				if (tile.updateComparator())
				{
					world.updateComparatorOutputLevel(pos, state.getBlock());
				}
			}
		}

		super.breakBlock(world, pos, state);
	}

	public ItemStack createStack(IBlockState state, @Nullable TileEntity tile)
	{
		ItemStack stack = new ItemStack(this);

		if (dropSpecial(state) && tile instanceof TileBase)
		{
			NBTTagCompound nbt = ((TileBase) tile).createItemData();

			if (!nbt.hasNoTags())
			{
				stack.setTagCompound(nbt);
			}
		}

		return stack;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if (world.isRemote && stack.hasTagCompound() && hasTileEntity(state) && stack.getTagCompound().hasKey("BlockEntityTag"))
		{
			TileEntity tileEntity = world.getTileEntity(pos);

			if (tileEntity != null)
			{
				tileEntity.handleUpdateTag(stack.getTagCompound().getCompoundTag("BlockEntityTag"));
			}
		}
	}
}