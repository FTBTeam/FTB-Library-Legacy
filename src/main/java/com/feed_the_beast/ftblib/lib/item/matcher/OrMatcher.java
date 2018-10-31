package com.feed_the_beast.ftblib.lib.item.matcher;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public class OrMatcher implements IItemMatcher
{
	public final List<IItemMatcher> matchers = new ArrayList<>();

	@Override
	public NBTBase toNBT(boolean forceTagCompound)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList list = new NBTTagList();

		for (IItemMatcher matcher : matchers)
		{
			if (matcher.isValid())
			{
				list.appendTag(matcher.toNBT(true));
			}
		}

		nbt.setTag("or", list);
		return nbt;
	}

	@Override
	public void fromNBT(NBTBase nbt)
	{
		matchers.clear();

		if (nbt instanceof NBTTagCompound)
		{
			NBTTagList list = ((NBTTagCompound) nbt).getTagList("or", Constants.NBT.TAG_COMPOUND);

			for (int i = 0; i < list.tagCount(); i++)
			{
				IItemMatcher matcher = ItemMatcherRegistry.createMatcher(list.getCompoundTagAt(i));

				if (matcher.isValid())
				{
					matchers.add(matcher);
				}
			}
		}
	}

	@Override
	public boolean test(ItemStack stack)
	{
		for (IItemMatcher matcher : matchers)
		{
			if (matcher.isValid() && !matcher.test(stack))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean isValid()
	{
		for (IItemMatcher matcher : matchers)
		{
			if (matcher.isValid())
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public void clearCache()
	{
		for (IItemMatcher matcher : matchers)
		{
			matcher.clearCache();
		}
	}

	@Override
	public void getAllStacks(Collection<ItemStack> stacks)
	{
		for (IItemMatcher matcher : matchers)
		{
			matcher.getAllStacks(stacks);
		}
	}
}