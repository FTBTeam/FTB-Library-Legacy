package com.feed_the_beast.ftbl.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public interface IContainerProvider
{
	@Nullable
	Container getContainer(EntityPlayer player, BlockPos pos, @Nullable NBTTagCompound nbt);
}