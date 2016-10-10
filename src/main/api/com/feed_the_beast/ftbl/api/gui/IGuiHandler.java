package com.feed_the_beast.ftbl.api.gui;

import com.feed_the_beast.ftbl.api.IRegistryObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public interface IGuiHandler extends IRegistryObject
{
    ResourceLocation getID();

    @Nullable
    Container getContainer(EntityPlayer player, @Nullable NBTTagCompound data);

    @Nullable
    Object getGui(EntityPlayer player, @Nullable NBTTagCompound data);
}