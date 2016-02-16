package ftb.lib.api.tile;

import ftb.lib.api.friends.LMWorldMP;
import ftb.lib.api.item.LMInvUtils;
import latmod.lib.MathHelperLM;
import net.minecraft.block.state.IBlockState;
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
	
	public void readTileData(NBTTagCompound tag)
	{
		super.readTileData(tag);
		LMInvUtils.readItemsFromNBT(items, tag, "Items");
		customName = tag.getString("CustomName");
	}
	
	public void writeTileData(NBTTagCompound tag)
	{
		super.writeTileData(tag);
		LMInvUtils.writeItemsToNBT(items, tag, "Items");
		if(!customName.isEmpty()) tag.setString("CustomName", customName);
	}
	
	public void onBroken(IBlockState state)
	{
		if(isServer() && dropItems && items.length > 0)
			LMInvUtils.dropAllItems(worldObj, getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D, items);
		
		markDirty();
		super.onBroken(state);
	}
	
	public String getName()
	{ return customName; }
	
	public void setName(String s)
	{ customName = s; }
	
	public void openInventory(EntityPlayer ep) { }
	
	public void closeInventory(EntityPlayer ep) { }
	
	public ItemStack decrStackSize(int i, int j)
	{ return LMInvUtils.decrStackSize(this, i, j); }
	
	public int getInventoryStackLimit()
	{ return 64; }
	
	public int getSizeInventory()
	{ return items.length; }
	
	public ItemStack getStackInSlot(int i)
	{ return items[i]; }
	
	public ItemStack removeStackFromSlot(int i)
	{ return LMInvUtils.removeStackFromSlot(this, i); }
	
	public void setInventorySlotContents(int i, ItemStack is)
	{ items[i] = is; }
	
	public boolean isUseableByPlayer(EntityPlayer ep)
	{ return getPrivacyLevel().canInteract(LMWorldMP.inst.getPlayer(ownerID), LMWorldMP.inst.getPlayer(ep)); }
	
	public boolean isItemValidForSlot(int i, ItemStack is)
	{ return true; }
	
	public int getField(int id)
	{ return 0; }
	
	public void setField(int id, int value)
	{
	}
	
	public int getFieldCount()
	{ return 0; }
	
	public void clear()
	{ LMInvUtils.clear(this); }
}