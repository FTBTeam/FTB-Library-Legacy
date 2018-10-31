package com.feed_the_beast.ftblib.lib.item.matcher;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * @author LatvianModder
 */
public interface IItemMatcher extends Predicate<ItemStack>
{
	NBTBase toNBT(boolean forceTagCompound);

	void fromNBT(NBTBase nbt);

	@Override
	boolean test(ItemStack stack);

	boolean isValid();

	void clearCache();

	void getAllStacks(Collection<ItemStack> stacks);
}