package com.feed_the_beast.ftblib.lib.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author LatvianModder
 */
public class ItemEntryWithCount
{
	public static final ItemEntryWithCount EMPTY = new ItemEntryWithCount(ItemEntry.EMPTY, 0)
	{
		@Override
		public boolean isEmpty()
		{
			return true;
		}
	};

	public final ItemEntry entry;
	public int count;

	public ItemEntryWithCount(ItemEntry e, int c)
	{
		entry = e;
		count = c;
	}

	public ItemEntryWithCount(NBTTagCompound nbt)
	{
		ItemStack stack = new ItemStack(nbt);
		entry = ItemEntry.get(stack);
		count = nbt.hasKey("RealCount") ? nbt.getInteger("RealCount") : stack.getCount();
	}

	public boolean isEmpty()
	{
		return count <= 0 || entry.isEmpty();
	}

	public NBTTagCompound serializeNBT()
	{
		if (isEmpty())
		{
			return new NBTTagCompound();
		}

		NBTTagCompound nbt = entry.getStack(1, false).serializeNBT();

		if (count > 1)
		{
			nbt.setInteger("RealCount", count);
		}

		return nbt;
	}

	public ItemStack getStack(boolean copy)
	{
		return entry.getStack(count, copy);
	}
}