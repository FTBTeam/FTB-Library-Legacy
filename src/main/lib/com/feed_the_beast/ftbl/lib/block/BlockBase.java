package com.feed_the_beast.ftbl.lib.block;

import com.feed_the_beast.ftbl.lib.tile.TileBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
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
					ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
					NBTTagCompound nbt = ((TileBase) tileEntity).createItemData();

					if (!nbt.hasNoTags())
					{
						stack.setTagCompound(nbt);
					}

					spawnAsEntity(worldIn, pos, stack);
				}

				if (tile.updateComparator())
				{
					worldIn.updateComparatorOutputLevel(pos, state.getBlock());
				}
			}
		}

		super.breakBlock(worldIn, pos, state);
	}

	public ItemStack createStack(TileBase tile)
	{
		ItemStack stack = new ItemStack(this);

		if (dropSpecial(tile.getBlockState()))
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