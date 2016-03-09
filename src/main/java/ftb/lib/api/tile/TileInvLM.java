package ftb.lib.api.tile;

import ftb.lib.api.item.LMInvUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.*;

public class TileInvLM extends TileLM
{
	public ItemStackHandler itemHandler;
	public boolean dropItems = true;
	
	public TileInvLM(int size)
	{
		itemHandler = createHandler(size);
	}
	
	protected ItemStackHandler createHandler(int size)
	{
		return new ItemStackHandler(size)
		{
			protected void onContentsChanged(int slot)
			{
				super.onContentsChanged(slot);
				TileInvLM.this.markDirty();
			}
		};
	}
	
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		return super.hasCapability(capability, facing);
	}
	
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) itemHandler;
		return super.getCapability(capability, facing);
	}
	
	public void readTileData(NBTTagCompound tag)
	{
		super.readTileData(tag);
		itemHandler.deserializeNBT(tag.getCompoundTag("Items"));
	}
	
	public void writeTileData(NBTTagCompound tag)
	{
		super.writeTileData(tag);
		tag.setTag("Items", itemHandler.serializeNBT());
	}
	
	public void onBroken(IBlockState state)
	{
		if(dropItems && isServer() && itemHandler != null && itemHandler.getSlots() > 0)
		{
			for(int i = 0; i < itemHandler.getSlots(); i++)
			{
				ItemStack item = itemHandler.getStackInSlot(i);
				
				if(item != null && item.stackSize > 0)
				{
					LMInvUtils.dropItem(worldObj, getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D, item, 10);
				}
			}
		}
		
		markDirty();
		super.onBroken(state);
	}
}