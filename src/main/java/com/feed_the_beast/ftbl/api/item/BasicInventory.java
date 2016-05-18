package com.feed_the_beast.ftbl.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

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
    public ItemStack removeStackFromSlot(int index)
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
    public void openInventory(EntityPlayer player) { }
    
    @Override
    public void closeInventory(EntityPlayer player) { }
    
    @Override
    public boolean isItemValidForSlot(int i, ItemStack is)
    { return true; }
    
    @Override
    public int getField(int id)
    { return 0; }
    
    @Override
    public void setField(int id, int value) { }
    
    @Override
    public int getFieldCount()
    { return 0; }
    
    @Override
    public void clear()
    { LMInvUtils.clear(this); }
    
    @Override
    public String getName()
    { return ""; }
    
    @Override
    public boolean hasCustomName()
    { return false; }
    
    @Override
    public ITextComponent getDisplayName()
    { return new TextComponentString(getName()); }
}