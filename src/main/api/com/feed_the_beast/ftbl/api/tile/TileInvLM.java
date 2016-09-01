package com.feed_the_beast.ftbl.api.tile;

import com.feed_the_beast.ftbl.api.item.LMInvUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileInvLM extends TileLM
{
    public final ItemStackHandler itemHandler;

    public TileInvLM(int size)
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
                TileInvLM.this.markDirty();
            }
        };
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);

    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return (T) itemHandler;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void writeTileData(NBTTagCompound nbt)
    {
        super.writeTileData(nbt);
        nbt.setTag("Items", itemHandler.serializeNBT());
    }

    @Override
    public void readTileData(NBTTagCompound nbt)
    {
        super.readTileData(nbt);
        itemHandler.deserializeNBT(nbt.getCompoundTag("Items"));
    }

    @Override
    public void writeTileClientData(NBTTagCompound nbt)
    {
        super.writeTileClientData(nbt);
        nbt.setTag("INV", itemHandler.serializeNBT());
    }

    @Override
    public void readTileClientData(NBTTagCompound nbt)
    {
        super.readTileClientData(nbt);
        itemHandler.deserializeNBT(nbt.getCompoundTag("INV"));
    }

    public void dropItems()
    {
        if(getSide().isServer() && itemHandler != null && itemHandler.getSlots() > 0)
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
    }
}