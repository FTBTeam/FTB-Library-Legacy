package com.feed_the_beast.ftbl.lib.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

/**
 * @author LatvianModder
 */
public final class ItemEntry
{
	public final Item item;
	public final int metadata;
	public final NBTTagCompound nbt;

	public ItemEntry(ItemStack stack)
	{
		item = stack.getItem();
		metadata = stack.getMetadata();
		NBTTagCompound nbt0 = stack.getTagCompound();
		nbt = (nbt0 == null || nbt0.hasNoTags()) ? null : nbt0;
	}

	public int hashCode()
	{
		return Objects.hash(item, metadata, nbt);
	}

	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		else if (o != null && o.getClass() == ItemEntry.class)
		{
			ItemEntry entry = (ItemEntry) o;
			return item == entry.item && metadata == entry.metadata && Objects.equals(nbt, entry.nbt);
		}
		return false;
	}
}