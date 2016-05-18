package com.feed_the_beast.ftbl.api.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    public ItemStack slotClick(int p_184996_1_, int dragType, ClickType clickTypeIn, EntityPlayer player)
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