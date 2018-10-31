package com.feed_the_beast.ftblib.lib.item.matcher;

import com.feed_the_beast.ftblib.lib.item.ItemStackSerializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collection;
import java.util.Objects;

/**
 * @author LatvianModder
 */
public class ItemStackMatcher implements IItemMatcher
{
	public static final ItemStackMatcher EMPTY = new ItemStackMatcher()
	{
		@Override
		public boolean test(ItemStack stack)
		{
			return stack.isEmpty();
		}
	};

	private ItemStack stack = ItemStack.EMPTY;
	private boolean ignoreMetadata = false;
	private boolean ignoreNBT = false;

	private Item item;
	private int meta;
	private NBTTagCompound nbt;

	public void setStack(ItemStack is)
	{
		stack = is;
		clearCache();
	}

	public void setIgnoreMetadata(boolean v)
	{
		ignoreMetadata = v;
		clearCache();
	}

	public void setIgnoreNBT(boolean v)
	{
		ignoreNBT = v;
		clearCache();
	}

	@Override
	public NBTBase toNBT(boolean forceTagCompound)
	{
		if (ignoreMetadata || ignoreNBT || forceTagCompound)
		{
			NBTTagCompound nbt = (NBTTagCompound) ItemStackSerializer.write(stack, true);

			if (ignoreMetadata)
			{
				nbt.setBoolean("ignoreMeta", true);
			}

			if (ignoreNBT)
			{
				nbt.setBoolean("ignoreNBT", true);
			}

			return nbt;
		}

		return null;
	}

	@Override
	public void fromNBT(NBTBase nbt)
	{
		stack = ItemStackSerializer.read(nbt);

		if (nbt instanceof NBTTagCompound)
		{
			NBTTagCompound n = (NBTTagCompound) nbt;
			ignoreMetadata = n.getBoolean("ignoreMeta");
			ignoreNBT = n.getBoolean("ignoreNBT");
		}
		else
		{
			ignoreMetadata = false;
			ignoreNBT = false;
		}
	}

	@Override
	public boolean test(ItemStack is)
	{
		if (is == stack)
		{
			return true;
		}

		if (item == null)
		{
			item = stack.getItem();
			meta = ignoreMetadata ? 0 : stack.getMetadata();
			nbt = ignoreNBT ? null : item.getNBTShareTag(stack);
		}

		return is.getItem() == item && (ignoreMetadata || is.getMetadata() == meta) && (ignoreNBT || Objects.equals(is.getItem().getNBTShareTag(is), nbt));
	}

	@Override
	public boolean isValid()
	{
		return !stack.isEmpty();
	}

	@Override
	public void clearCache()
	{
		item = null;
		meta = 0;
		nbt = null;
	}

	@Override
	public void getAllStacks(Collection<ItemStack> stacks)
	{
		stacks.add(stack);
	}
}