package com.feed_the_beast.ftblib.events.player;

import com.feed_the_beast.ftblib.events.FTBLibEvent;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;

/**
 * @author LatvianModder
 */
public abstract class ForgePlayerEvent extends FTBLibEvent
{
	private final ForgePlayer player;

	public ForgePlayerEvent(ForgePlayer p)
	{
		player = p;
	}

	public ForgePlayer getPlayer()
	{
		return player;
	}
}