package com.feed_the_beast.ftblib.events.player;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;

/**
 * @author LatvianModder
 */
public class ForgePlayerLoggedOutEvent extends ForgePlayerEvent
{
	public ForgePlayerLoggedOutEvent(ForgePlayer player)
	{
		super(player);
	}
}