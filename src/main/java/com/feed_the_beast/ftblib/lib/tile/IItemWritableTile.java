package com.feed_the_beast.ftblib.lib.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public interface IItemWritableTile
{
	void writeToItem(ItemStack stack);

	void readFromItem(ItemStack stack);

	default void writeToPickBlock(ItemStack stack)
	{
		writeToItem(stack);
	}

	default boolean canBeWrenched(EntityPlayer player)
	{
		return true;
	}
}