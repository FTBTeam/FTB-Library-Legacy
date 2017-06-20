package com.feed_the_beast.ftbl.lib.tile;

import com.feed_the_beast.ftbl.lib.util.InvUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileInvBase extends TileBase
{
	public final ItemStackHandler itemHandler;

	public TileInvBase(int size)
	{
		itemHandler = createHandler(size);
	}

	protected ItemStackHandler createHandler(int size)
	{
		return new ItemStackHandler(size)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				super.onContentsChanged(slot);
				TileInvBase.this.markDirty();
			}
		};
	}

	@Override
	protected boolean updateComparator()
	{
		return true;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return (T) itemHandler;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	protected void writeData(NBTTagCompound nbt, EnumSaveType type)
	{
		nbt.setTag("Items", itemHandler.serializeNBT());
	}

	@Override
	protected void readData(NBTTagCompound nbt, EnumSaveType type)
	{
		itemHandler.deserializeNBT(nbt.getCompoundTag("Items"));
	}

	public void dropItems()
	{
		InvUtils.dropAllItems(world, getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D, itemHandler);
	}
}