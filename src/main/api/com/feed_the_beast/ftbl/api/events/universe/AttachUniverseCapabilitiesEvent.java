package com.feed_the_beast.ftbl.api.events.universe;

import com.feed_the_beast.ftbl.api.IUniverse;
import net.minecraftforge.event.AttachCapabilitiesEvent;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class AttachUniverseCapabilitiesEvent extends AttachCapabilitiesEvent
{
    private final IUniverse world;

    public AttachUniverseCapabilitiesEvent(IUniverse w)
    {
        super(w);
        world = w;
    }

    public IUniverse getWorld()
    {
        return world;
    }
}