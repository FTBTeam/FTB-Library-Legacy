package com.feed_the_beast.ftbl.api.tile;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IGuiTile extends ITileEntity
{
	Container getContainer(EntityPlayer ep, NBTTagCompound data);
	
	@SideOnly(Side.CLIENT)
	GuiScreen getGui(EntityPlayer ep, NBTTagCompound data);
}