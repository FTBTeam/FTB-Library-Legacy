package com.feed_the_beast.ftbl.api.events.universe;

import com.feed_the_beast.ftbl.api.IUniverse;

/**
 * @author LatvianModder
 */
public class ForgeUniverseLoadedEvent extends ForgeUniverseEvent
{
    public ForgeUniverseLoadedEvent(IUniverse universe)
    {
        super(universe);
    }
}