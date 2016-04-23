package ftb.lib.api.tile;

import ftb.lib.api.item.LMInvUtils;
import latmod.lib.MathHelperLM;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileInvLM extends TileLM implements IInventory
{
	private String customName = "";
	public final ItemStack[] items;
	public boolean dropItems = true;
	
	public final int[] ALL_SLOTS;
	
	public TileInvLM(int invSize)
	{
		items = new ItemStack[invSize];
		ALL_SLOTS = MathHelperLM.getAllInts(0, invSize);
	}
	
	@Override
	public void readTileData(NBTTagCompound tag)
	{
		super.readTileData(tag);
		LMInvUtils.readItemsFromNBT(items, tag, "Items");
		customName = tag.getString("CustomName");
	}
	
	@Override
	public void writeTileData(NBTTagCompound tag)
	{
		super.writeTileData(tag);
		LMInvUtils.writeItemsToNBT(items, tag, "Items");
		if(!customName.isEmpty()) tag.setString("CustomName", customName);
	}
	
	@Override
	public void onBroken()
	{
		if(isServer() && dropItems && items.length > 0)
			LMInvUtils.dropAllItems(worldObj, xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, items);
		
		markDirty();
		super.onBroken();
	}
	
	@Override
	public String getInventoryName()
	{ return customName; }
	
	@Override
	public void setName(String s)
	{ customName = s; }
	
	@Override
	public void openInventory() { }
	
	@Override
	public void closeInventory() { }
	
	@Override
	public ItemStack decrStackSize(int i, int j)
	{ return LMInvUtils.decrStackSize(this, i, j); }
	
	@Override
	public int getInventoryStackLimit()
	{ return 64; }
	
	@Override
	public int getSizeInventory()
	{ return items.length; }
	
	@Override
	public ItemStack getStackInSlot(int i)
	{ return items[i]; }
	
	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{ return LMInvUtils.removeStackFromSlot(this, i); }
	
	@Override
	public void setInventorySlotContents(int i, ItemStack is)
	{ items[i] = is; }
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer ep)
	{ return security.canInteract(ep); }
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack is)
	{ return true; }
	
	public void clear()
	{ LMInvUtils.clear(this); }
	
	@Override
	public boolean hasCustomInventoryName()
	{ return !getInventoryName().isEmpty(); }
	
	public final String getName()
	{ return getInventoryName(); }
}