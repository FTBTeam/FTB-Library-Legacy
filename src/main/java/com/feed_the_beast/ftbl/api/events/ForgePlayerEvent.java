package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.api.ForgePlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

/**
 * Created by LatvianModder on 18.05.2016.
 */
public abstract class ForgePlayerEvent extends Event
{
    public static class AttachCapabilities extends AttachCapabilitiesEvent
    {
        public final ForgePlayer player;

        public AttachCapabilities(ForgePlayer p)
        {
            super(p);
            player = p;
        }
    }

    public static class Sync extends ForgePlayerEvent
    {
        public final boolean self;
        public final NBTTagCompound data;

        public Sync(ForgePlayer p, NBTTagCompound d, boolean s)
        {
            super(p);
            data = d;
            self = s;
        }
    }

    public static class LoggedIn extends ForgePlayerEvent
    {
        public final boolean first;

        public LoggedIn(ForgePlayer p, boolean f)
        {
            super(p);
            first = f;
        }
    }

    public static class LoggedOut extends ForgePlayerEvent
    {
        public LoggedOut(ForgePlayer p)
        {
            super(p);
        }
    }

    public static class OnDeath extends ForgePlayerEvent
    {
        public OnDeath(ForgePlayer p)
        {
            super(p);
        }
    }

    public static class AddInfo extends ForgePlayerEvent
    {
        public final List<ITextComponent> list;
        public final long currentTime;

        public AddInfo(ForgePlayer p, List<ITextComponent> l, long t)
        {
            super(p);
            list = l;
            currentTime = t;
        }
    }

    public final ForgePlayer player;

    public ForgePlayerEvent(ForgePlayer p)
    {
        player = p;
    }
}