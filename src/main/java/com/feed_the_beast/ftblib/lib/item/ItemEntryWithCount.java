package com.feed_the_beast.ftblib.lib.item;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

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
		if (nbt.hasKey("I", Constants.NBT.TAG_STRING))
		{
			Item item = Item.REGISTRY.getObjectById(nbt.getInteger("I"));

			if (item != null && item != Items.AIR)
			{
				int size = nbt.getInteger("S");
				int meta = nbt.getShort("M");
				NBTTagCompound tag = (NBTTagCompound) nbt.getTag("N");
				NBTTagCompound caps = (NBTTagCompound) nbt.getTag("C");
				entry = new ItemEntry(item, meta, tag, caps);
				count = size == 0 ? 1 : size;
			}
			else
			{
				entry = ItemEntry.EMPTY;
				count = 0;
			}
		}
		else
		{
			ItemStack stack = new ItemStack(nbt);
			entry = ItemEntry.get(stack);
			count = nbt.hasKey("RealCount") ? nbt.getInteger("RealCount") : stack.getCount();
		}
	}

	public boolean isEmpty()
	{
		return count <= 0 || entry.isEmpty();
	}

	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();

		if (isEmpty())
		{
			return nbt;
		}

		nbt.setInteger("I", Item.REGISTRY.getIDForObject(entry.item));

		if (count != 1)
		{
			if (count <= 127)
			{
				nbt.setByte("S", (byte) count);
			}
			else
			{
				nbt.setInteger("S", count);
			}
		}

		if (entry.metadata != 0)
		{
			nbt.setShort("M", (short) entry.metadata);
		}

		if (entry.nbt != null)
		{
			nbt.setTag("N", entry.nbt);
		}

		if (entry.caps != null)
		{
			nbt.setTag("C", entry.caps);
		}

		return nbt;
	}

	public ItemStack getStack(boolean copy)
	{
		return entry.getStack(count, copy);
	}
}