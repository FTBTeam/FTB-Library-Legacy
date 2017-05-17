package com.feed_the_beast.ftbl.api.events.team;

import com.feed_the_beast.ftbl.api.IForgeTeam;

/**
 * @author LatvianModder
 */
public class ForgeTeamDeletedEvent extends ForgeTeamEvent
{
    public ForgeTeamDeletedEvent(IForgeTeam team)
    {
        super(team);
    }
}