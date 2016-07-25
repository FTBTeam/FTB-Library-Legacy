package com.feed_the_beast.ftbl.api.tile;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by LatvianModder on 14.05.2016.
 */
public interface ITileClientAction
{
    void onAction(TileEntity te, NBTTagCompound data, EntityPlayerMP player);
}