package com.feed_the_beast.ftbl.api.events.player;

import com.feed_the_beast.ftbl.api.IForgePlayer;

/**
 * @author LatvianModder
 */
public class ForgePlayerLoggedOutEvent extends ForgePlayerEvent
{
	public ForgePlayerLoggedOutEvent(IForgePlayer player)
	{
		super(player);
	}
}