package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.paint.IPaintable;
import com.feed_the_beast.ftbl.api.paint.IPainterItem;
import com.feed_the_beast.ftbl.api.paint.PainterItemStorage;
import com.feed_the_beast.ftbl.api.paint.SinglePaintStorage;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * Created by LatvianModder on 15.05.2016.
 */
public class FTBLibCapabilities
{
    private static boolean enabled = false;
    
    @CapabilityInject(IPaintable.class)
    public static Capability<IPaintable> PAINTABLE_TILE_CAPABILITY = null;
    
    @CapabilityInject(IPainterItem.class)
    public static Capability<IPainterItem> PAINTER_ITEM_CAPABILITY = null;
    
    public static void enable()
    {
        if(enabled)
        {
            return;
        }
        
        enabled = true;
        
        CapabilityManager.INSTANCE.register(IPaintable.class, new Capability.IStorage<IPaintable>()
        {
            @Override
            public NBTBase writeNBT(Capability<IPaintable> capability, IPaintable instance, EnumFacing side)
            {
                IBlockState paint = instance.getPaint(side);
                return new NBTTagInt(paint == null ? 0 : Block.getStateId(paint));
            }
            
            @Override
            public void readNBT(Capability<IPaintable> capability, IPaintable instance, EnumFacing side, NBTBase base)
            {
                int paint = ((NBTTagInt) base).getInt();
                instance.setPaint(side, paint == 0 ? null : Block.getStateById(paint));
            }
        }, () -> {
            return new SinglePaintStorage();
        });
        
        CapabilityManager.INSTANCE.register(IPainterItem.class, new Capability.IStorage<IPainterItem>()
        {
            @Override
            public NBTBase writeNBT(Capability<IPainterItem> capability, IPainterItem instance, EnumFacing side)
            {
                IBlockState paint = instance.getPaint();
                return new NBTTagInt(paint == null ? 0 : Block.getStateId(paint));
            }
            
            @Override
            public void readNBT(Capability<IPainterItem> capability, IPainterItem instance, EnumFacing side, NBTBase base)
            {
                int paint = ((NBTTagInt) base).getInt();
                instance.setPaint(paint == 0 ? null : Block.getStateById(paint));
            }
        }, () -> {
            return new PainterItemStorage();
        });
    }
}