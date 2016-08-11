package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.util.FTBLib;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 12.08.2016.
 */
public class SyncDataEvent extends Event
{
    private final IForgePlayer player;
    private final NBTTagCompound data;
    private final boolean login;

    private SyncDataEvent(IForgePlayer p, NBTTagCompound d, boolean l)
    {
        player = p;
        data = d;
        login = l;
    }

    public static NBTTagCompound generateData(IForgePlayer player, boolean login)
    {
        SyncDataEvent event = new SyncDataEvent(player, new NBTTagCompound(), login);
        MinecraftForge.EVENT_BUS.post(event);

        if(FTBLib.DEV_ENV)
        {
            FTBLib.dev_logger.info("Synced data TX: " + event.getData());
        }

        return event.getData();
    }

    @SideOnly(Side.CLIENT)
    public static void readData(NBTTagCompound data, boolean login)
    {
        SyncDataEvent event = new SyncDataEvent(null, data, login);
        MinecraftForge.EVENT_BUS.post(event);

        if(FTBLib.DEV_ENV)
        {
            FTBLib.dev_logger.info("Synced data RX: " + data);
        }
    }

    @Nullable
    public IForgePlayer getPlayer()
    {
        return player;
    }

    public Side getSide()
    {
        return player == null ? Side.CLIENT : Side.SERVER;
    }

    public NBTTagCompound getData()
    {
        return data;
    }

    public boolean isAtLogin()
    {
        return login;
    }
}