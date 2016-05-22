package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.api.ForgePlayer;
import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgeWorld;
import com.feed_the_beast.ftbl.api.ForgeWorldMP;
import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.util.FTBLib;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 18.05.2016.
 */
public abstract class ForgeWorldEvent extends Event
{
    public static class AttachCapabilities extends AttachCapabilitiesEvent
    {
        public final ForgeWorld world;

        public AttachCapabilities(ForgeWorld w)
        {
            super(w);
            world = w;
        }
    }

    public static class Sync extends ForgeWorldEvent
    {
        public final NBTTagCompound syncData;
        public final ForgePlayer self;
        public final boolean login;

        private Sync(ForgeWorld w, NBTTagCompound d, ForgePlayer s, boolean l)
        {
            super(w);
            syncData = d;
            self = s;
            login = l;
        }

        public static NBTTagCompound generateData(ForgePlayerMP p, boolean login)
        {
            Sync event = new Sync(ForgeWorldMP.inst, new NBTTagCompound(), p, login);
            MinecraftForge.EVENT_BUS.post(event);

            if(FTBLib.DEV_ENV)
            {
                FTBLib.dev_logger.info("Synced data TX: " + event.syncData);
            }

            return event.syncData;
        }

        @SideOnly(Side.CLIENT)
        public static void readData(NBTTagCompound data, boolean login)
        {
            Sync event = new Sync(ForgeWorldSP.inst, data, ForgeWorldSP.inst.clientPlayer, login);
            MinecraftForge.EVENT_BUS.post(event);

            if(FTBLib.DEV_ENV)
            {
                FTBLib.dev_logger.info("Synced data RX: " + data);
            }
        }
    }

    public static class OnLoaded extends ForgeWorldEvent
    {
        public OnLoaded(ForgeWorld w)
        {
            super(w);
        }
    }

    public static class OnLoadedBeforePlayers extends ForgeWorldEvent
    {
        public OnLoadedBeforePlayers(ForgeWorld w)
        {
            super(w);
        }
    }

    public static class OnPostLoaded extends ForgeWorldEvent
    {
        public OnPostLoaded(ForgeWorld w)
        {
            super(w);
        }
    }

    public static class OnClosed extends ForgeWorldEvent
    {
        public OnClosed(ForgeWorld w)
        {
            super(w);
        }
    }

    public final ForgeWorld world;

    public ForgeWorldEvent(ForgeWorld w)
    {
        world = w;
    }
}