package com.feed_the_beast.ftbl.api.events.team;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;

/**
 * @author LatvianModder
 */
public class ForgeTeamOwnerChangedEvent extends ForgeTeamEvent
{
    private final IForgePlayer oldOwner;
    private final IForgePlayer newOwner;

    public ForgeTeamOwnerChangedEvent(IForgeTeam team, IForgePlayer o0, IForgePlayer o1)
    {
        super(team);
        oldOwner = o0;
        newOwner = o1;
    }

    public IForgePlayer getOldOwner()
    {
        return oldOwner;
    }

    public IForgePlayer getNewOwner()
    {
        return newOwner;
    }
}