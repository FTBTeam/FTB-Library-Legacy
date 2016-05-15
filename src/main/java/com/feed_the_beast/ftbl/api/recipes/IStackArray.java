package com.feed_the_beast.ftbl.api.recipes;

import net.minecraft.item.ItemStack;

public interface IStackArray
{
	boolean matches(ItemStack[] ai);
	IStackArray[] getItems();
}