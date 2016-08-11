package com.feed_the_beast.ftbl.api.events.team;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class ForgeTeamPlayerLeftEvent extends ForgeTeamEvent
{
    private final IForgePlayer player;

    public ForgeTeamPlayerLeftEvent(IForgeTeam t, IForgePlayer p)
    {
        super(t);
        player = p;
    }

    public IForgePlayer getPlayer()
    {
        return player;
    }
}