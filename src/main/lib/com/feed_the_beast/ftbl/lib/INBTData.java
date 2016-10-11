package com.feed_the_beast.ftbl.lib;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 10.10.2016.
 */
public interface INBTData
{
    ResourceLocation getID();

    void writeData(NBTTagCompound tag);

    void readData(NBTTagCompound tag);
}