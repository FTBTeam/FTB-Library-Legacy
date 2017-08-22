package com.feed_the_beast.ftbl.lib.block;

import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;

/**
 * @author LatvianModder
 */
public class ItemBlockBase extends ItemBlock
{
	public ItemBlockBase(Block b, boolean hasSubtypes)
	{
		super(b);
		Preconditions.checkNotNull(b);
		Preconditions.checkNotNull(b.getRegistryName());
		setRegistryName(b.getRegistryName());

		if (hasSubtypes)
		{
			setHasSubtypes(true);
			setMaxDamage(0);
		}
	}

	public ItemBlockBase(Block b)
	{
		this(b, false);
	}

	@Override
	public int getMetadata(int metadata)
	{
		return getHasSubtypes() ? metadata : 0;
	}

	public boolean renderPlacement(ItemStack stack, EntityPlayer player, RayTraceResult ray)
	{
		return false;
	}
}