package com.feed_the_beast.ftbl.api;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by LatvianModder on 10.09.2016.
 */
public interface ISyncData
{
    NBTTagCompound writeSyncData(EntityPlayerMP player, IForgePlayer forgePlayer);

    void readSyncData(NBTTagCompound nbt);
}