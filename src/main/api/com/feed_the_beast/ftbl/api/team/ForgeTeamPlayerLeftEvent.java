package com.feed_the_beast.ftbl.api.team;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;

/**
 * @author LatvianModder
 */
public class ForgeTeamPlayerLeftEvent extends ForgeTeamEvent
{
	private final IForgePlayer player;

	public ForgeTeamPlayerLeftEvent(IForgeTeam team, IForgePlayer p)
	{
		super(team);
		player = p;
	}

	public IForgePlayer getPlayer()
	{
		return player;
	}
}