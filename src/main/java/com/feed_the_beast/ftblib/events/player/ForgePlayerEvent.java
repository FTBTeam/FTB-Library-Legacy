package com.feed_the_beast.ftblib.events.player;

import com.feed_the_beast.ftblib.events.universe.UniverseEvent;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;

/**
 * @author LatvianModder
 */
public abstract class ForgePlayerEvent extends UniverseEvent
{
	private final ForgePlayer player;

	public ForgePlayerEvent(ForgePlayer p)
	{
		super(p.universe);
		player = p;
	}

	public ForgePlayer getPlayer()
	{
		return player;
	}
}