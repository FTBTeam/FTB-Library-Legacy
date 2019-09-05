package com.feed_the_beast.ftblib.events.team;

import com.feed_the_beast.ftblib.lib.data.ForgeTeam;

/**
 * @author LatvianModder
 */
public class ForgeTeamChangedEvent extends ForgeTeamEvent
{
	private final ForgeTeam oldTeam;

	public ForgeTeamChangedEvent(ForgeTeam team, ForgeTeam o)
	{
		super(team);
		oldTeam = o;
	}

	public ForgeTeam getOldTeam()
	{
		return oldTeam;
	}
}