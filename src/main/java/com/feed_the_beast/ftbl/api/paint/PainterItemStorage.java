package com.feed_the_beast.ftbl.api.paint;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by LatvianModder on 15.05.2016.
 */
public class PainterItemStorage implements IPainterItem
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
}
