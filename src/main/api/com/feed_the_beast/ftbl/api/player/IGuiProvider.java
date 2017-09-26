package com.feed_the_beast.ftbl.api.player;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public interface IGuiProvider
{
	@Nullable
	GuiScreen getGui(EntityPlayer player, BlockPos pos, @Nullable NBTTagCompound data);
}