package com.feed_the_beast.ftblib.events.player;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;

/**
 * @author LatvianModder
 */
public class ForgePlayerLoggedInEvent extends ForgePlayerEvent
{
	private final boolean first;

	public ForgePlayerLoggedInEvent(ForgePlayer player, boolean f)
	{
		super(player);
		first = f;
	}

	public boolean isFirstLogin()
	{
		return first;
	}
}