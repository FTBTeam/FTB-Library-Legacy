package com.feed_the_beast.ftbl.api.events.team;

import com.feed_the_beast.ftbl.api.IForgeTeam;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class ForgeTeamDeletedEvent extends ForgeTeamEvent
{
    public ForgeTeamDeletedEvent(IForgeTeam t)
    {
        super(t);
    }
}