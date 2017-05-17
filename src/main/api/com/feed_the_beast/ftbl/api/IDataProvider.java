package com.feed_the_beast.ftbl.api;

import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author LatvianModder
 */
public interface IDataProvider<T>
{
    INBTSerializable<NBTBase> getData(T owner);
}