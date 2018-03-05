package com.feed_the_beast.ftblib.events.player;

import com.feed_the_beast.ftblib.events.team.ForgeTeamEvent;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;

/**
 * @author LatvianModder
 */
public abstract class ForgePlayerEvent extends ForgeTeamEvent
{
	private final ForgePlayer player;

	public ForgePlayerEvent(ForgePlayer p)
	{
		super(p.team);
		player = p;
	}

	public ForgePlayer getPlayer()
	{
		return player;
	}
}