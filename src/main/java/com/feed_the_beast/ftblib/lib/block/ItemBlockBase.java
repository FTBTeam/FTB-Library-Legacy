package com.feed_the_beast.ftblib.lib.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import java.util.Objects;

/**
 * @author LatvianModder
 */
public class ItemBlockBase extends ItemBlock
{
	public ItemBlockBase(Block b, boolean hasSubtypes)
	{
		super(b);
		Objects.requireNonNull(b);
		Objects.requireNonNull(b.getRegistryName());
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
}