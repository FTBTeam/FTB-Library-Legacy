package com.feed_the_beast.ftbl.api.events.universe;

import com.feed_the_beast.ftbl.api.IUniverse;

/**
 * @author LatvianModder
 */
public class ForgeUniverseLoadedBeforePlayersEvent extends ForgeUniverseEvent
{
    public ForgeUniverseLoadedBeforePlayersEvent(IUniverse universe)
    {
        super(universe);
    }
}