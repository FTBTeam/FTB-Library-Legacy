package ftb.lib.api.tile;

import cpw.mods.fml.relauncher.*;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;

public interface IGuiTile
{
	Container getContainer(EntityPlayer ep, NBTTagCompound data);
	
	@SideOnly(Side.CLIENT)
	GuiScreen getGui(EntityPlayer ep, NBTTagCompound data);
}