package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.api.ForgePlayer;
import com.feed_the_beast.ftbl.api.ForgeWorld;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

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
        public final NBTTagCompound data;
        public final ForgePlayer self;
        public final boolean login;

        public Sync(ForgeWorld w, NBTTagCompound d, ForgePlayer s, boolean l)
        {
            super(w);
            data = d;
            self = s;
            login = l;
        }
    }

    public static class OnLoaded extends ForgeWorldEvent
    {
        public OnLoaded(ForgeWorld w)
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