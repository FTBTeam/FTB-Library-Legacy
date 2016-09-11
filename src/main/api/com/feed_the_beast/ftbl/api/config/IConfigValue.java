package com.feed_the_beast.ftbl.api.config;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.IJsonSerializable;
import net.minecraftforge.common.util.INBTSerializable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by LatvianModder on 11.09.2016.
 */
public interface IConfigValue extends INBTSerializable<NBTBase>, IJsonSerializable
{
    IConfigValueProvider getProvider();

    void writeData(DataOutput data, boolean extended) throws IOException;

    void readData(DataInput data, boolean extended) throws IOException;

    String getString();

    boolean getBoolean();

    int getInt();

    IConfigValue copy();
}