package com.feed_the_beast.ftbl.api.tile;

import com.feed_the_beast.ftbl.api.item.LMInvUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

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
            @Override
            protected void onContentsChanged(int slot)
            {
                super.onContentsChanged(slot);
                TileInvLM.this.markDirty();
            }
        };
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nonnull EnumFacing facing)
    {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nonnull
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing facing)
    {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return (T) itemHandler;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void readTileData(@Nonnull NBTTagCompound tag)
    {
        super.readTileData(tag);
        itemHandler.deserializeNBT(tag.getCompoundTag("Items"));
    }

    @Override
    public void writeTileData(@Nonnull NBTTagCompound tag)
    {
        super.writeTileData(tag);
        tag.setTag("Items", itemHandler.serializeNBT());
    }

    @Override
    public void onBroken(@Nonnull IBlockState state)
    {
        if(dropItems && getSide().isServer() && itemHandler != null && itemHandler.getSlots() > 0)
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