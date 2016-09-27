package com.feed_the_beast.ftbl.lib;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Created by LatvianModder on 06.08.2016.
 */
public class EmptyCapStorage<T> implements Capability.IStorage<T>
{
    @Override
    public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side)
    {
        return null;
    }

    @Override
    public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt)
    {
    }
}