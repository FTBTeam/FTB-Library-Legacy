package ftb.lib.api.gui;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public interface IClientActionTile
{
	void onClientAction(EntityPlayerMP ep, String action, NBTTagCompound data);
}