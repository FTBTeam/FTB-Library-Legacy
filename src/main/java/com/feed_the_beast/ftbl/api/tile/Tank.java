package com.feed_the_beast.ftbl.api.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class Tank implements IFluidHandler
{
    public final String name;
    public FluidTank fluidTank;
    private int prevAmount = -1;
    
    public Tank(String s, double buckets)
    {
        name = s;
        fluidTank = new FluidTank((int) (buckets * 1000));
    }
    
    public void setCapacity(double buckets)
    { fluidTank.setCapacity((int) (buckets * 1000)); }
    
    public boolean hasFluid(int amt)
    { return getAmount() >= amt; }
    
    public boolean hasFluid()
    { return hasFluid(1); }
    
    public boolean isEmpty()
    { return !hasFluid(); }
    
    public boolean isFull()
    { return hasFluid(getCapacity()); }
    
    public FluidStack getFluidStack()
    { return fluidTank.getFluid(); }
    
    public Fluid getFluid()
    { return hasFluid() ? getFluidStack().getFluid() : null; }
    
    public int getAmount()
    { return fluidTank.getFluidAmount(); }
    
    public double getAmountD()
    { return getAmount() / (double) getCapacity(); }
    
    public int getCapacity()
    { return fluidTank.getCapacity(); }
    
    public void readFromNBT(NBTTagCompound tag)
    {
        NBTTagCompound tankTag = tag.getCompoundTag(name);
        fluidTank.readFromNBT(tankTag);
        if(tankTag.hasKey("Empty")) { fluidTank.setFluid(null); }
        
        if(fluidTank.getFluidAmount() > fluidTank.getCapacity())
        {
            FluidStack fs = fluidTank.getFluid().copy();
            fs.amount = fluidTank.getCapacity();
            fluidTank.setFluid(fs);
        }
        
        checkIfChanged();
    }
    
    public void writeToNBT(NBTTagCompound tag)
    {
        NBTTagCompound tankTag = new NBTTagCompound();
        fluidTank.writeToNBT(tankTag);
        tag.setTag(name, tankTag);
    }
    
    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
        if(canFill(from, resource.getFluid()))
        {
            int i = fluidTank.fill(resource, doFill);
            if(doFill) { checkIfChanged(); }
            return i;
        }
        return 0;
    }
    
    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
    {
        if(fluidTank.getFluidAmount() <= 0) { return null; }
        if(!fluidTank.getFluid().isFluidEqual(resource)) { return null; }
        if(!canDrain(from, resource.getFluid())) { return null; }
        FluidStack fs = fluidTank.drain(resource.amount, doDrain);
        if(doDrain) { checkIfChanged(); }
        return fs;
    }
    
    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
        FluidStack fs = fluidTank.drain(maxDrain, doDrain);
        if(doDrain) { checkIfChanged(); }
        return fs;
    }
    
    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    { return true; }
    
    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    { return true; }
    
    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from)
    { return new FluidTankInfo[] {fluidTank.getInfo()}; }
    
    private void checkIfChanged()
    {
        if(prevAmount == -1 || prevAmount != getAmount())
        {
            prevAmount = getAmount();
            onFluidChanged();
        }
    }
    
    public void onFluidChanged()
    {
    }
}