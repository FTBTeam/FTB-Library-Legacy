package com.feed_the_beast.ftblib.events.team;

import com.feed_the_beast.ftblib.lib.data.ForgeTeam;

/**
 * @author LatvianModder
 */
public class ForgeTeamCreatedEvent extends ForgeTeamEvent
{
	public ForgeTeamCreatedEvent(ForgeTeam team)
	{
		super(team);
	}
}