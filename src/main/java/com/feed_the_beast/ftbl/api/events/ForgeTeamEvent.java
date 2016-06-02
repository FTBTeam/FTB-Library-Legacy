package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.api.ForgePlayer;
import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgeTeam;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by LatvianModder on 18.05.2016.
 */
public abstract class ForgeTeamEvent extends Event
{
    public static class AttachCapabilities extends AttachCapabilitiesEvent
    {
        public final ForgeTeam team;

        public AttachCapabilities(ForgeTeam t)
        {
            super(t);
            team = t;
        }
    }

    public static class Sync extends ForgeTeamEvent
    {
        public final Side side;
        public final NBTTagCompound data;
        public final ForgePlayer player;

        public Sync(ForgeTeam t, Side s, NBTTagCompound d, ForgePlayer o)
        {
            super(t);
            side = s;
            data = d;
            player = o;
        }
    }

    public static class GetSettings extends ForgeTeamEvent
    {
        public final ConfigGroup settings;

        public GetSettings(ForgeTeam p, ConfigGroup g)
        {
            super(p);
            settings = g;
        }
    }

    //TODO: Implement me
    public static class Created extends ForgeTeamEvent
    {
        public Created(ForgeTeam t)
        {
            super(t);
        }
    }

    public static class Deleted extends ForgeTeamEvent
    {
        public Deleted(ForgeTeam t)
        {
            super(t);
        }
    }

    public static class OwnerChanged extends ForgeTeamEvent
    {
        public final ForgePlayerMP oldOwner;
        public final ForgePlayerMP newOwner;

        public OwnerChanged(ForgeTeam t, ForgePlayerMP o0, ForgePlayerMP o1)
        {
            super(t);
            oldOwner = o0;
            newOwner = o1;
        }
    }

    //TODO: Implement me
    public static class PlayerJoined extends ForgeTeamEvent
    {
        public final ForgePlayerMP player;

        public PlayerJoined(ForgeTeam t, ForgePlayerMP p)
        {
            super(t);
            player = p;
        }
    }

    //TODO: Implement me
    public static class PlayerLeft extends ForgeTeamEvent
    {
        public final ForgePlayerMP player;

        public PlayerLeft(ForgeTeam t, ForgePlayerMP p)
        {
            super(t);
            player = p;
        }
    }

    public final ForgeTeam team;

    public ForgeTeamEvent(ForgeTeam t)
    {
        team = t;
    }
}