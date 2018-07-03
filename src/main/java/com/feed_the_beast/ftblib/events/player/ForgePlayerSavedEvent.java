package com.feed_the_beast.ftblib.events.player;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;

/**
 * @author LatvianModder
 */
public class ForgePlayerSavedEvent extends ForgePlayerEvent
{
	public ForgePlayerSavedEvent(ForgePlayer player)
	{
		super(player);
	}
}