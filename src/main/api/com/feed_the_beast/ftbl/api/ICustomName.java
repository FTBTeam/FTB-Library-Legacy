package com.feed_the_beast.ftbl.api;

import net.minecraft.util.text.ITextComponent;

/**
 * @author LatvianModder
 */
public interface ICustomName
{
	default boolean hasCustomName()
	{
		return true;
	}

	ITextComponent getCustomDisplayName();
}