package com.feed_the_beast.ftbl.api.team;

import com.feed_the_beast.ftbl.api.FTBLibEvent;
import com.feed_the_beast.ftbl.api.IForgeTeam;

import java.util.Objects;

/**
 * @author LatvianModder
 */
public class ForgeTeamEvent extends FTBLibEvent
{
	private final IForgeTeam team;

	public ForgeTeamEvent(IForgeTeam t)
	{
		team = Objects.requireNonNull(t, "Null IForgeTeam in ForgeTeamEvent!");
	}

	public IForgeTeam getTeam()
	{
		return team;
	}
}