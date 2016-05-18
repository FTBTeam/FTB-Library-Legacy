package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.api.ForgeWorld;
import com.feed_the_beast.ftbl.api.ForgeWorldMP;
import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.util.FTBLib;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 23.04.2016.
 */
public class SyncWorldEvent extends Event
{
    public final ForgeWorld world;
    public final EntityPlayer player;
    public final NBTTagCompound syncData;
    public final boolean login;
    
    private SyncWorldEvent(ForgeWorld w, EntityPlayer ep, NBTTagCompound t, boolean b)
    {
        world = w;
        player = ep;
        syncData = t;
        login = b;
    }
    
    public static NBTTagCompound generateData(EntityPlayerMP ep, boolean login)
    {
        SyncWorldEvent event = new SyncWorldEvent(ForgeWorldMP.inst, ep, new NBTTagCompound(), login);
        MinecraftForge.EVENT_BUS.post(event);
        
        if(FTBLib.DEV_ENV) { FTBLib.dev_logger.info("Synced data TX: " + event.syncData); }
        
        return event.syncData;
    }
    
    @SideOnly(Side.CLIENT)
    public static void readData(NBTTagCompound data, boolean login)
    {
        SyncWorldEvent event = new SyncWorldEvent(ForgeWorldSP.inst, FTBLibClient.mc.thePlayer, data, login);
        MinecraftForge.EVENT_BUS.post(event);
        
        if(FTBLib.DEV_ENV) { FTBLib.dev_logger.info("Synced data RX: " + data); }
    }
}