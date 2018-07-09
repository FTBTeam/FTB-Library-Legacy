package com.feed_the_beast.ftblib.lib.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class NBTUtils
{
	@SuppressWarnings("ConstantConditions")
	public static void renameTag(NBTTagCompound nbt, String oldName, String newName)
	{
		NBTBase tag = nbt.getTag(oldName);

		if (tag != null)
		{
			nbt.removeTag(oldName);
			nbt.setTag(newName, tag);
		}
	}

	public static boolean hasBlockData(ItemStack stack)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		return nbt != null && nbt.hasKey("BlockEntityTag");
	}

	public static NBTTagCompound getBlockData(ItemStack stack)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		return nbt != null ? nbt.getCompoundTag("BlockEntityTag") : new NBTTagCompound();
	}

	public static void removeBlockData(ItemStack stack)
	{
		NBTTagCompound nbt = stack.getTagCompound();

		if (nbt != null)
		{
			nbt.removeTag("BlockEntityTag");
			nbt.getCompoundTag("display").removeTag("Lore");
			stack.setTagCompound(minimize(nbt));
		}
	}

	public static void copyTags(@Nullable NBTTagCompound from, @Nullable NBTTagCompound to)
	{
		if (from != null && to != null && !from.hasNoTags())
		{
			for (String key : from.getKeySet())
			{
				to.setTag(key, from.getTag(key));
			}
		}
	}

	@Nullable
	public static NBTTagCompound minimize(@Nullable NBTTagCompound nbt)
	{
		if (nbt == null || nbt.hasNoTags())
		{
			return null;
		}

		NBTTagCompound nbt1 = null;

		for (String key : nbt.getKeySet())
		{
			NBTBase nbt2 = nbt.getTag(key);

			if (nbt2 instanceof NBTTagCompound)
			{
				nbt2 = minimize((NBTTagCompound) nbt2);
			}
			else if (nbt2 instanceof NBTTagList)
			{
				nbt2 = minimize((NBTTagList) nbt2);
			}

			if (nbt2 != null)
			{
				if (nbt1 == null)
				{
					nbt1 = new NBTTagCompound();
				}

				nbt1.setTag(key, nbt2);
			}
		}

		return nbt1;
	}

	@Nullable
	public static NBTTagList minimize(@Nullable NBTTagList nbt)
	{
		return nbt == null || nbt.hasNoTags() ? null : nbt;
	}
}