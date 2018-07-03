package com.feed_the_beast.ftblib.lib.util.misc;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author LatvianModder
 */
public class NBTCapStorage<T> implements Capability.IStorage<T>
{
	@Override
	public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side)
	{
		return instance instanceof INBTSerializable ? ((INBTSerializable) instance).serializeNBT() : null;
	}

	@Override
	public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt)
	{
		if (nbt != null && instance instanceof INBTSerializable)
		{
			((INBTSerializable) instance).deserializeNBT(nbt);
		}
	}
}