package com.feed_the_beast.ftblib.lib;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author LatvianModder
 */
public interface IDataProvider<T>
{
	INBTSerializable<NBTTagCompound> getData(T owner);
}