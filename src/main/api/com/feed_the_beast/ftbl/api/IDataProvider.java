package com.feed_the_beast.ftbl.api;

import net.minecraft.nbt.NBTBase;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by LatvianModder on 13.11.2016.
 */
public interface IDataProvider<T>
{
    INBTSerializable<NBTBase> getData(T owner);
}