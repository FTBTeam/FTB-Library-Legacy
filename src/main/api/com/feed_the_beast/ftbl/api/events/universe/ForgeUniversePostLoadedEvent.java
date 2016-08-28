package com.feed_the_beast.ftbl.api.events.universe;

import com.feed_the_beast.ftbl.api.IUniverse;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class ForgeUniversePostLoadedEvent extends ForgeUniverseEvent
{
    public ForgeUniversePostLoadedEvent(IUniverse w)
    {
        super(w);
    }
}