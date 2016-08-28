package com.feed_the_beast.ftbl.api.events.universe;

import com.feed_the_beast.ftbl.api.IUniverse;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by LatvianModder on 18.05.2016.
 */
public abstract class ForgeUniverseEvent extends Event
{
    private final IUniverse world;

    public ForgeUniverseEvent(IUniverse w)
    {
        world = w;
    }

    public IUniverse getWorld()
    {
        return world;
    }
}