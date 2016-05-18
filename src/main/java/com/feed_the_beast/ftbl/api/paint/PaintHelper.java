package com.feed_the_beast.ftbl.api.paint;

import com.feed_the_beast.ftbl.FTBLibCapabilities;
import com.feed_the_beast.ftbl.api.LangKey;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;

public class PaintHelper
{
    public static final LangKey texture_set = new LangKey("ftbl.paint.texture_set");
    public static final LangKey texture_cleared = new LangKey("ftbl.paint.texture_cleared");
    
    public static IBlockState getPaintFromBlock(IBlockAccess w, IBlockState state, RayTraceResult hit)
    {
        if(state.getBlock() instanceof ICustomPaintBlock)
        {
            return ((ICustomPaintBlock) state.getBlock()).getCustomPaint(w, state, hit);
        }
        
        return (!state.getBlock().hasTileEntity(state) && state.isFullCube()) ? state : null;
    }
    
    public static ActionResult<ItemStack> onItemRightClick(ItemStack is, EntityPlayer ep)
    {
        if(!ep.worldObj.isRemote && ep.isSneaking() && is.hasCapability(FTBLibCapabilities.PAINTER_ITEM_CAPABILITY, null))
        {
            IPainterItem painterItem = is.getCapability(FTBLibCapabilities.PAINTER_ITEM_CAPABILITY, null);
            
            if(painterItem.getPaint() != null)
            {
                painterItem.setPaint(null);
                texture_cleared.printChat(ep);
                return new ActionResult<>(EnumActionResult.SUCCESS, is);
            }
        }
        
        return new ActionResult<>(EnumActionResult.PASS, is);
    }
    
    public static EnumActionResult onItemUse(ItemStack is, EntityPlayer ep, RayTraceResult hit)
    {
        if(ep.worldObj.isRemote || !is.hasCapability(FTBLibCapabilities.PAINTER_ITEM_CAPABILITY, null))
        {
            return EnumActionResult.SUCCESS;
        }
        
        IPainterItem painterItem = is.getCapability(FTBLibCapabilities.PAINTER_ITEM_CAPABILITY, null);
        
        TileEntity te = ep.worldObj.getTileEntity(hit.getBlockPos());
        
        if(te != null && te.hasCapability(FTBLibCapabilities.PAINTABLE_TILE_CAPABILITY, hit.sideHit))
        {
            IPaintable paintable = te.getCapability(FTBLibCapabilities.PAINTABLE_TILE_CAPABILITY, hit.sideHit);
            IBlockState paint = painterItem.getPaint();
            
            if(ep.isSneaking())
            {
                if(painterItem.canPaintBlocks(is))
                {
                    boolean b = false;
                    
                    for(EnumFacing f : EnumFacing.VALUES)
                    {
                        if(paintable.canSetPaint(ep, f, paint))
                        {
                            paintable.setPaint(f, paint);
                            b = true;
                        }
                    }
                    
                    if(b)
                    {
                        painterItem.damagePainter(is, ep);
                        te.markDirty();
                    }
                }
            }
            else
            {
                if(painterItem.canPaintBlocks(is) && paintable.canSetPaint(ep, hit.sideHit, paint))
                {
                    paintable.setPaint(hit.sideHit, paint);
                    painterItem.damagePainter(is, ep);
                    te.markDirty();
                }
            }
        }
        else if(ep.isSneaking() && !ep.worldObj.isAirBlock(hit.getBlockPos()))
        {
            IBlockState state = ep.worldObj.getBlockState(hit.getBlockPos());
            
            if(state.isFullCube())
            {
                IBlockState blockPaint = getPaintFromBlock(ep.worldObj, state, hit);
                
                if(blockPaint != painterItem.getPaint())
                {
                    painterItem.setPaint(blockPaint);
                    texture_set.printChat(ep, new ItemStack(blockPaint.getBlock(), 1, blockPaint.getBlock().getMetaFromState(blockPaint)).getDisplayName());
                }
            }
        }
        
        return EnumActionResult.SUCCESS;
    }
}