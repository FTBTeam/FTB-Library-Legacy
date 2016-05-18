package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.api.gui.ContainerEmpty;
import com.feed_the_beast.ftbl.api.gui.LMGuiHandler;
import com.feed_the_beast.ftbl.api.item.ItemDisplay;
import com.feed_the_beast.ftbl.gui.GuiDisplayItem;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FTBLibGuiHandler extends LMGuiHandler
{
    public static final FTBLibGuiHandler instance = new FTBLibGuiHandler("FTBL");
    
    public static final int DISPLAY_ITEM = 1;
    public static final int SECURITY = 2;
    
    public FTBLibGuiHandler(String s)
    { super(s); }
    
    @Override
    public Container getContainer(EntityPlayer ep, int id, NBTTagCompound data)
    {
        return new ContainerEmpty(ep, null);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer ep, int id, NBTTagCompound data)
    {
        if(id == DISPLAY_ITEM) { return new GuiDisplayItem(ItemDisplay.readFromNBT(data)); }
        return null;
    }
}