package com.feed_the_beast.ftblib.lib.block;

import com.feed_the_beast.ftblib.lib.tile.IItemWritableTile;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class BlockSpecialDrop extends Block
{
	public BlockSpecialDrop(Material material, MapColor color)
	{
		super(material, color);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity tileEntity, ItemStack stack)
	{
		if (!(tileEntity instanceof IItemWritableTile))
		{
			super.harvestBlock(world, player, pos, state, null, stack);
			return;
		}

		player.addStat(StatList.getBlockStats(this));
		player.addExhaustion(0.005F);

		if (world.isRemote)
		{
			return;
		}

		Item item = getItemDropped(state, world.rand, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack));

		if (item == Items.AIR)
		{
			return;
		}

		ItemStack itemstack = new ItemStack(item, quantityDropped(world.rand));
		((IItemWritableTile) tileEntity).writeToItem(itemstack);
		spawnAsEntity(world, pos, itemstack);
	}

	@Override
	@Deprecated
	public ItemStack getItem(World world, BlockPos pos, IBlockState state)
	{
		ItemStack stack = super.getItem(world, pos, state);
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof IItemWritableTile)
		{
			((IItemWritableTile) tileEntity).writeToPickBlock(stack);
		}

		return stack;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if (hasTileEntity(state))
		{
			TileEntity tile = world.getTileEntity(pos);

			if (tile instanceof IItemWritableTile)
			{
				((IItemWritableTile) tile).readFromItem(stack);
			}
		}
	}
}