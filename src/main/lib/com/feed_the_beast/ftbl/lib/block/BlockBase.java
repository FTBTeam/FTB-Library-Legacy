package com.feed_the_beast.ftbl.lib.block;

import com.feed_the_beast.ftbl.lib.tile.TileBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author LatvianModder
 */
public class BlockBase extends Block
{
	public BlockBase(String mod, String id, Material material, MapColor color)
	{
		super(material, color);
		setRegistryName(mod + ':' + id);
		setUnlocalizedName(mod + '.' + id);
		setHardness(1.8F);
		setCreativeTab(CreativeTabs.MISC);
	}

	public boolean dropSpecial(IBlockState state)
	{
		return false;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		if (dropSpecial(state))
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);

			if (tileEntity instanceof TileBase)
			{
				((TileBase) tileEntity).destroyedByCreativePlayer = player.capabilities.isCreativeMode;
			}
		}
	}

	@Override
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
	{
		if (!dropSpecial(state))
		{
			super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
		}
	}

	@Override
	@Deprecated
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return isNormalCube(state, world, pos);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (dropSpecial(state))
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);

			if (tileEntity instanceof TileBase)
			{
				TileBase tile = (TileBase) tileEntity;

				if (tile.shouldDrop())
				{
					spawnAsEntity(worldIn, pos, createStack(state, ((TileBase) tileEntity)));
				}

				if (tile.updateComparator())
				{
					worldIn.updateComparatorOutputLevel(pos, state.getBlock());
				}
			}
		}

		super.breakBlock(worldIn, pos, state);
	}

	public ItemStack createStack(IBlockState state, TileBase tile)
	{
		ItemStack stack = new ItemStack(this);

		if (dropSpecial(state))
		{
			NBTTagCompound nbt = tile.createItemData();

			if (!nbt.hasNoTags())
			{
				stack.setTagCompound(nbt);
			}
		}

		return stack;
	}
}