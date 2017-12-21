package com.feed_the_beast.ftblib.events.team;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;

/**
 * @author LatvianModder
 */
public class ForgeTeamPlayerLeftEvent extends ForgeTeamEvent
{
	private final ForgePlayer player;

	public ForgeTeamPlayerLeftEvent(ForgeTeam team, ForgePlayer p)
	{
		super(team);
		player = p;
	}

	public ForgePlayer getPlayer()
	{
		return player;
	}
}