package com.feed_the_beast.ftbl.api.events.universe;

import com.feed_the_beast.ftbl.api.IUniverse;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class ForgeUniverseClosedEvent extends ForgeUniverseEvent
{
    public ForgeUniverseClosedEvent(IUniverse universe)
    {
        super(universe);
    }
}