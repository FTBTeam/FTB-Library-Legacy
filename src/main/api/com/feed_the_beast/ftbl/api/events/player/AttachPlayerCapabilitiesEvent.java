package com.feed_the_beast.ftbl.api.events.player;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class AttachPlayerCapabilitiesEvent extends AttachCapabilitiesEvent
{
    private final IForgePlayer player;

    public AttachPlayerCapabilitiesEvent(IForgePlayer p)
    {
        super(p);
        player = p;
    }

    public IForgePlayer getPlayer()
    {
        return player;
    }
}