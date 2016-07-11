package com.feed_the_beast.ftbl.api.client.gui;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.net.MessageOpenGui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class LMGuiHandler
{
    public final String ID;

    public LMGuiHandler(String s)
    {
        ID = s;
    }

    public abstract Container getContainer(@Nonnull EntityPlayer ep, int id, @Nullable NBTTagCompound data);

    @SideOnly(Side.CLIENT)
    public abstract GuiScreen getGui(@Nonnull EntityPlayer ep, int id, @Nullable NBTTagCompound data);

    public void openGui(@Nonnull EntityPlayer ep, int id, @Nullable NBTTagCompound data)
    {
        if(ep.worldObj.isRemote)
        {
            FTBLibMod.proxy.openClientGui(ep, ID, id, data);
        }
        else
        {
            Container c = getContainer(ep, id, data);

            EntityPlayerMP epM = (EntityPlayerMP) ep;
            epM.getNextWindowId();
            epM.closeContainer();

            if(c != null)
            {
                epM.openContainer = c;
            }

            epM.openContainer.windowId = epM.currentWindowId;
            epM.openContainer.addListener(epM);
            new MessageOpenGui(ID, id, data, epM.currentWindowId).sendTo(epM);
        }
    }
}