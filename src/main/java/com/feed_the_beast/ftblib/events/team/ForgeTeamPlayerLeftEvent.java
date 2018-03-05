package com.feed_the_beast.ftblib.events.team;

import com.feed_the_beast.ftblib.events.player.ForgePlayerEvent;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;

/**
 * @author LatvianModder
 */
public class ForgeTeamPlayerLeftEvent extends ForgePlayerEvent
{
	public ForgeTeamPlayerLeftEvent(ForgePlayer player)
	{
		super(player);
	}
}