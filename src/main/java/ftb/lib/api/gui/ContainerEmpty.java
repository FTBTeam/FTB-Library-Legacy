package ftb.lib.api.gui;

import cpw.mods.fml.relauncher.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ContainerEmpty extends ContainerLM
{
	public ContainerEmpty(EntityPlayer ep, Object inv)
	{ super(ep, inv); }
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer ep, int i)
	{ return null; }
	
	@Override
	public void detectAndSendChanges()
	{
	}
	
	@Override
	public void onContainerClosed(EntityPlayer ep)
	{
	}
	
	@Override
	public ItemStack slotClick(int i, int j, int k, EntityPlayer ep)
	{
		return null;
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory inv)
	{
	}
	
	@Override
	public void putStackInSlot(int i, ItemStack is)
	{
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void putStacksInSlots(ItemStack[] is)
	{
	}
}