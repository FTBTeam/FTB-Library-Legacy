package com.feed_the_beast.ftblib.events.player;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;

/**
 * @author LatvianModder
 */
public class ForgePlayerLoggedInEvent extends ForgePlayerEvent
{
	public ForgePlayerLoggedInEvent(ForgePlayer player)
	{
		super(player);
	}
}