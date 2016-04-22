package ftb.lib.api.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.*;

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
	public ItemStack func_184996_a(int p_184996_1_, int dragType, ClickType clickTypeIn, EntityPlayer player)
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