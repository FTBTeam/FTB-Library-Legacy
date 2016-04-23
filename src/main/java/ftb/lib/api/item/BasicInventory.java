package ftb.lib.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class BasicInventory implements IInventory
{
	public final ItemStack[] items;
	
	public BasicInventory(int i)
	{ items = new ItemStack[i]; }
	
	@Override
	public int getSizeInventory()
	{ return items.length; }
	
	@Override
	public ItemStack getStackInSlot(int i)
	{ return items[i]; }
	
	@Override
	public ItemStack decrStackSize(int slot, int amt)
	{ return LMInvUtils.decrStackSize(this, slot, amt); }
	
	@Override
	public ItemStack getStackInSlotOnClosing(int index)
	{ return LMInvUtils.removeStackFromSlot(this, index); }
	
	@Override
	public void setInventorySlotContents(int i, ItemStack is)
	{ items[i] = is; }
	
	@Override
	public int getInventoryStackLimit()
	{ return 64; }
	
	@Override
	public void markDirty() { }
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer ep)
	{ return true; }
	
	@Override
	public void openInventory() { }
	
	@Override
	public void closeInventory() { }
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack is)
	{ return true; }
	
	public void clear()
	{ LMInvUtils.clear(this); }
	
	@Override
	public String getInventoryName()
	{ return ""; }
	
	@Override
	public boolean hasCustomInventoryName()
	{ return false; }
}