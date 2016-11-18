package com.feed_the_beast.ftbl.api.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 13.11.2016.
 */
public interface IContainerProvider
{
    @Nullable
    Container getContainer(EntityPlayer player, BlockPos pos, @Nullable NBTTagCompound nbt);
}