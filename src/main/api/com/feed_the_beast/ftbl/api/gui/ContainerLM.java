package com.feed_the_beast.ftbl.api.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public abstract class ContainerLM extends Container
{
    public final EntityPlayer player;

    public ContainerLM(EntityPlayer ep)
    {
        player = ep;
    }

    @Nullable
    public abstract IItemHandler getItemHandler();

    @Override
    public ItemStack transferStackInSlot(EntityPlayer ep, int i)
    {
        int nonPlayerSlots = getNonPlayerSlots();

        if(nonPlayerSlots <= 0)
        {
            return null;
        }

        ItemStack is = null;
        Slot slot = inventorySlots.get(i);

        if(slot != null && slot.getHasStack())
        {
            ItemStack is1 = slot.getStack();
            is = is1.copy();

            if(i < nonPlayerSlots)
            {
                if(!mergeItemStack(is1, nonPlayerSlots, inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if(!mergeItemStack(is1, 0, nonPlayerSlots, false))
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

    protected int getNonPlayerSlots()
    {
        IItemHandler itemHandler = getItemHandler();
        return (itemHandler == null) ? 0 : itemHandler.getSlots();
    }

    public void addPlayerSlots(int posX, int posY, boolean ignoreCurrent)
    {
        if(player.inventory != null)
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
    public boolean canInteractWith(EntityPlayer ep)
    {
        return true;
    }
}