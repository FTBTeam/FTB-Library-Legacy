package com.feed_the_beast.ftbl.api.events.team;

import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.events.FTBLibEvent;
import com.google.common.base.Preconditions;

/**
 * @author LatvianModder
 */
public class ForgeTeamEvent extends FTBLibEvent
{
	private final IForgeTeam team;

	public ForgeTeamEvent(IForgeTeam t)
	{
		team = Preconditions.checkNotNull(t, "Null IForgeTeam in ForgeTeamEvent!");
	}

	public IForgeTeam getTeam()
	{
		return team;
	}
}