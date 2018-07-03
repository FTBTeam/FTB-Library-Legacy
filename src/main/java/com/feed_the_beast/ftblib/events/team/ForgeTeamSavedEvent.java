package com.feed_the_beast.ftblib.events.team;

import com.feed_the_beast.ftblib.lib.data.ForgeTeam;

/**
 * @author LatvianModder
 */
public class ForgeTeamSavedEvent extends ForgeTeamEvent
{
	public ForgeTeamSavedEvent(ForgeTeam team)
	{
		super(team);
	}
}