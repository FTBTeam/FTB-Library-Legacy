package com.feed_the_beast.ftbl.api.events.team;

import com.feed_the_beast.ftbl.api.IForgeTeam;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class ForgeTeamCreatedEvent extends ForgeTeamEvent
{
    public ForgeTeamCreatedEvent(IForgeTeam team)
    {
        super(team);
    }
}