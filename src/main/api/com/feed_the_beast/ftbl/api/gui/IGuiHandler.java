package com.feed_the_beast.ftbl.api.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public interface IGuiHandler
{
    @Nullable
    Container getContainer(EntityPlayer player, @Nullable NBTTagCompound data);

    @Nullable
    GuiScreen getGui(EntityPlayer player, @Nullable NBTTagCompound data);
}