package com.feed_the_beast.ftbl.api.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public abstract class ContainerLM extends Container
{
    public final EntityPlayer player;
    public IItemHandler itemHandler;

    public ContainerLM(EntityPlayer ep, IItemHandler i)
    {
        player = ep;
        itemHandler = i;
    }

    public void updateMainHandItem()
    {
        updateSlot(player.inventory.currentItem);
    }

    public void updateSlot(int i)
    {
        for(IContainerListener l : listeners)
        {
            l.sendSlotContents(this, i, getSlot(i).getStack());
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer ep, int i)
    {
        ItemStack is = null;
        Slot slot = inventorySlots.get(i);

        if(slot != null && slot.getHasStack())
        {
            ItemStack is1 = slot.getStack();
            is = is1.copy();

            if(i < itemHandler.getSlots())
            {
                if(!mergeItemStack(is1, itemHandler.getSlots(), inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if(!mergeItemStack(is1, 0, itemHandler.getSlots(), false))
            {
                return null;
            }

            if(is1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return is;
    }

    public void addPlayerSlots(int posX, int posY, boolean ignoreCurrent)
    {
        if(player != null && player.inventory != null)
        {
            for(int y = 0; y < 3; y++)
            {
                for(int x = 0; x < 9; x++)
                {
                    addSlotToContainer(new Slot(player.inventory, x + y * 9 + 9, posX + x * 18, posY + y * 18));
                }
            }

            int i = ignoreCurrent ? player.inventory.currentItem : -1;

            for(int x = 0; x < 9; x++)
            {
                if(x != i)
                {
                    addSlotToContainer(new Slot(player.inventory, x, posX + x * 18, posY + 58));
                }
                else
                {
                    addSlotToContainer(new Slot(player.inventory, x, posX + x * 18, posY + 58)
                    {
                        @Override
                        public boolean canTakeStack(EntityPlayer ep)
                        {
                            return false;
                        }
                    });
                }
            }
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer ep)
    {
        return true;
    }

    @Override
    protected boolean mergeItemStack(ItemStack is, int min, int max, boolean RtoL)
    {
        boolean flag1 = false;
        int k = min;
        if(RtoL)
        {
            k = max - 1;
        }
        Slot slot;
        ItemStack is1;

        if(is.isStackable())
        {
            while(is.stackSize > 0 && (!RtoL && k < max || RtoL && k >= min))
            {
                slot = inventorySlots.get(k);
                is1 = slot.getStack();

                if(slot.isItemValid(is) && slot.inventory.isItemValidForSlot(k, is))
                {
                    if(is1 != null && is1.getItem() == is.getItem() && (!is.getHasSubtypes() || is.getItemDamage() == is1.getItemDamage()) && ItemStack.areItemStackTagsEqual(is, is1))
                    {
                        int l = is1.stackSize + is.stackSize;

                        if(l <= is.getMaxStackSize())
                        {
                            is.stackSize = 0;
                            is1.stackSize = l;
                            slot.onSlotChanged();
                            flag1 = true;
                        }
                        else if(is1.stackSize < is.getMaxStackSize())
                        {
                            is.stackSize -= is.getMaxStackSize() - is1.stackSize;
                            is1.stackSize = is.getMaxStackSize();
                            slot.onSlotChanged();
                            flag1 = true;
                        }
                    }
                }

                if(RtoL)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        if(is.stackSize > 0)
        {
            if(RtoL)
            {
                k = max - 1;
            }
            else
            {
                k = min;
            }

            while(!RtoL && k < max || RtoL && k >= min)
            {
                slot = inventorySlots.get(k);
                is1 = slot.getStack();

                if(is1 == null)
                {
                    slot.putStack(is.copy());
                    slot.onSlotChanged();
                    is.stackSize = 0;
                    flag1 = true;
                    break;
                }

                if(RtoL)
                {
                    --k;
                }
                else
                {
                    ++k;
                }
            }
        }

        return flag1;
    }
}