package com.feed_the_beast.ftblib.events.player;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;

/**
 * @author LatvianModder
 */
public class ForgePlayerLoadedEvent extends ForgePlayerEvent
{
	public ForgePlayerLoadedEvent(ForgePlayer player)
	{
		super(player);
	}
}