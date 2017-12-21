package com.feed_the_beast.ftblib.lib.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

/**
 * @author LatvianModder
 */
public final class ItemEntry
{
	public static final ItemEntry EMPTY = new ItemEntry(ItemStack.EMPTY);

	public static ItemEntry get(ItemStack stack)
	{
		return stack.isEmpty() ? EMPTY : new ItemEntry(stack);
	}

	public final Item item;
	public final int metadata;
	public final NBTTagCompound nbt;

	private ItemEntry(ItemStack stack)
	{
		item = stack.getItem();
		metadata = stack.getMetadata();
		NBTTagCompound nbt0 = stack.getTagCompound();
		nbt = (nbt0 == null || nbt0.hasNoTags()) ? null : nbt0;
	}

	public boolean isEmpty()
	{
		return this == EMPTY;
	}

	public int hashCode()
	{
		return Objects.hash(item, metadata, nbt);
	}

	public boolean equalsEntry(ItemEntry entry)
	{
		if (entry == EMPTY)
		{
			return this == EMPTY;
		}

		return item == entry.item && metadata == entry.metadata && Objects.equals(nbt, entry.nbt);
	}

	public boolean equals(Object o)
	{
		return o == this || o != null && o.getClass() == ItemEntry.class && equalsEntry((ItemEntry) o);
	}
}