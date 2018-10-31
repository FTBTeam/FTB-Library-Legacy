package com.feed_the_beast.ftblib.lib.item.matcher;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;

/**
 * @author LatvianModder
 */
public class OreNameMatcher implements IItemMatcher
{
	public String ore = "";

	@Override
	public NBTBase toNBT(boolean forceTagCompound)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("ore", ore);
		return nbt;
	}

	@Override
	public void fromNBT(NBTBase nbt)
	{
		if (nbt instanceof NBTTagCompound)
		{
			ore = ((NBTTagCompound) nbt).getString("ore");
		}
		else
		{
			ore = "";
		}
	}

	@Override
	public boolean test(ItemStack stack)
	{
		return false;
	}

	@Override
	public boolean isValid()
	{
		return !ore.isEmpty();
	}

	@Override
	public void clearCache()
	{
	}

	@Override
	public void getAllStacks(Collection<ItemStack> stacks)
	{
		stacks.addAll(OreDictionary.getOres(ore, false));
	}
}