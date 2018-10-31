package com.feed_the_beast.ftblib.lib.item.matcher;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;

import java.util.Collection;

/**
 * @author LatvianModder
 */
public final class NoItemMatcher implements IItemMatcher
{
	public static final NoItemMatcher INSTANCE = new NoItemMatcher();

	private NoItemMatcher()
	{
	}

	@Override
	public NBTBase toNBT(boolean forceTagCompound)
	{
		return new NBTTagString("");
	}

	@Override
	public void fromNBT(NBTBase nbt)
	{
	}

	@Override
	public boolean test(ItemStack stack)
	{
		return false;
	}

	@Override
	public boolean isValid()
	{
		return false;
	}

	@Override
	public void clearCache()
	{
	}

	@Override
	public void getAllStacks(Collection<ItemStack> stacks)
	{
	}
}