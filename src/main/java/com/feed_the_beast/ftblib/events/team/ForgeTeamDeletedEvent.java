package com.feed_the_beast.ftblib.events.team;

import com.feed_the_beast.ftblib.lib.data.ForgeTeam;

/**
 * @author LatvianModder
 */
public class ForgeTeamDeletedEvent extends ForgeTeamEvent
{
	public ForgeTeamDeletedEvent(ForgeTeam team)
	{
		super(team);
	}
}