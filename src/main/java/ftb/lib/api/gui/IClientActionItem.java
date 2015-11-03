package ftb.lib.api.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IClientActionItem
{
	public ItemStack onClientAction(ItemStack is, EntityPlayer ep, String action, NBTTagCompound data);
}