package com.feed_the_beast.ftblib.lib.data;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author LatvianModder
 */
public interface ISyncData
{
	NBTTagCompound writeSyncData(EntityPlayerMP player, ForgePlayer forgePlayer);

	void readSyncData(NBTTagCompound nbt);
}