package com.feed_the_beast.ftbl.api.paint;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by LatvianModder on 15.05.2016.
 */
public class PainterItemStorage implements IPainterItem, INBTSerializable<NBTTagInt>
{
	private IBlockState paint;
	
	@Override
	public IBlockState getPaint()
	{ return paint; }
	
	@Override
	public void setPaint(IBlockState p)
	{ paint = p; }
	
	@Override
	public boolean canPaintBlocks(ItemStack is)
	{ return !is.isItemStackDamageable() || is.getItemDamage() <= is.getMaxDamage(); }
	
	@Override
	public void damagePainter(ItemStack is, EntityPlayer ep)
	{ is.damageItem(1, ep); }
	
	@Override
	public NBTTagInt serializeNBT()
	{
		IBlockState p = getPaint();
		return new NBTTagInt(p == null ? 0 : Block.getStateId(p));
	}
	
	@Override
	public void deserializeNBT(NBTTagInt nbt)
	{
		int i = nbt.getInt();
		setPaint(i == 0 ? null : Block.getStateById(i));
	}
}
