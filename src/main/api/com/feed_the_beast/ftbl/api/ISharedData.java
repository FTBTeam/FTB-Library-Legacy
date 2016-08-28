package com.feed_the_beast.ftbl.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public interface ISharedData extends INBTSerializable<NBTTagCompound>
{
    Side getSide();

    IPackMode getPackMode();

    UUID getUniverseID();
}