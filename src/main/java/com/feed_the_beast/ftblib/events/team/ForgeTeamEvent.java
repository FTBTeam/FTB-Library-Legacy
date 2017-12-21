package com.feed_the_beast.ftblib.events.team;

import com.feed_the_beast.ftblib.events.FTBLibEvent;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;

import java.util.Objects;

/**
 * @author LatvianModder
 */
public class ForgeTeamEvent extends FTBLibEvent
{
	private final ForgeTeam team;

	public ForgeTeamEvent(ForgeTeam t)
	{
		team = Objects.requireNonNull(t, "Null IForgeTeam in ForgeTeamEvent!");
	}

	public ForgeTeam getTeam()
	{
		return team;
	}
}