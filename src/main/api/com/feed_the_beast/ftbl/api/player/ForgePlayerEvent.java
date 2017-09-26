package com.feed_the_beast.ftbl.api.player;

import com.feed_the_beast.ftbl.api.FTBLibEvent;
import com.feed_the_beast.ftbl.api.IForgePlayer;

/**
 * @author LatvianModder
 */
public abstract class ForgePlayerEvent extends FTBLibEvent
{
	private final IForgePlayer player;

	public ForgePlayerEvent(IForgePlayer p)
	{
		player = p;
	}

	public IForgePlayer getPlayer()
	{
		return player;
	}
}