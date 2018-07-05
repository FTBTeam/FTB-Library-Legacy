package com.feed_the_beast.ftblib.lib.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import java.util.Objects;

/**
 * @author LatvianModder
 */
public class ItemBlockBase extends ItemBlock
{
	public ItemBlockBase(Block block)
	{
		super(block);
		Objects.requireNonNull(block);
		Objects.requireNonNull(block.getRegistryName());
		setRegistryName(block.getRegistryName());
	}
}