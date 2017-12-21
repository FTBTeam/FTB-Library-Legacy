package com.feed_the_beast.ftblib.lib.util.misc;

import com.feed_the_beast.ftblib.lib.tile.EnumSaveType;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author LatvianModder
 */
public abstract class DataStorage
{
	public static final DataStorage EMPTY = new DataStorage()
	{
		@Override
		public void serializeNBT(NBTTagCompound nbt, EnumSaveType type)
		{
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt, EnumSaveType type)
		{
		}

		@Override
		public boolean isEmpty()
		{
			return true;
		}
	};

	public abstract void serializeNBT(NBTTagCompound nbt, EnumSaveType type);

	public abstract void deserializeNBT(NBTTagCompound nbt, EnumSaveType type);

	public boolean isEmpty()
	{
		return false;
	}
}