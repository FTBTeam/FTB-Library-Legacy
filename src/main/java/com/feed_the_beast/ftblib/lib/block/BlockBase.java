package com.feed_the_beast.ftblib.lib.block;

import com.feed_the_beast.ftblib.lib.tile.TileBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
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
		setTranslationKey(mod + '.' + id);
		setHardness(1.8F);
		setCreativeTab(CreativeTabs.MISC);
	}

	public boolean dropSpecial(IBlockState state)
	{
		return false;
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity tileEntity, ItemStack stack)
	{
		player.addStat(StatList.getBlockStats(this));
		player.addExhaustion(0.005F);

		if (dropSpecial(state) && (player.capabilities.isCreativeMode || tileEntity instanceof TileBase && ((TileBase) tileEntity).shouldDrop()) || EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0 && canSilkHarvest(world, pos, state, player))
		{
			java.util.List<ItemStack> items = new java.util.ArrayList<>();
			ItemStack itemstack = getSilkTouchDrop(state);

			if (!itemstack.isEmpty())
			{
				if (tileEntity instanceof TileBase)
				{
					((TileBase) tileEntity).writeToItem(itemstack);
				}

				items.add(itemstack);
			}

			net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, world, pos, state, 0, 1F, true, player);

			for (ItemStack item : items)
			{
				spawnAsEntity(world, pos, item);
			}
		}
		else
		{
			harvesters.set(player);
			dropBlockAsItem(world, pos, state, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack));
			harvesters.set(null);
		}
	}

	@Override
	@Deprecated
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face)
	{
		return isNormalCube(state, world, pos) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}

	public ItemStack createStack(IBlockState state, @Nullable TileEntity tile)
	{
		ItemStack stack = new ItemStack(this, 1, damageDropped(state));

		if (dropSpecial(state) && tile instanceof TileBase)
		{
			((TileBase) tile).writeToItem(stack);
		}

		return stack;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if (hasTileEntity(state))
		{
			TileEntity tile = world.getTileEntity(pos);

			if (tile instanceof TileBase)
			{
				((TileBase) tile).readFromItem(stack);
			}
		}
	}
}