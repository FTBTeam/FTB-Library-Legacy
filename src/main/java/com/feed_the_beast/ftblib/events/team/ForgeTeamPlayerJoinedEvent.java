package com.feed_the_beast.ftblib.events.team;

import com.feed_the_beast.ftblib.events.player.ForgePlayerEvent;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class ForgeTeamPlayerJoinedEvent extends ForgePlayerEvent
{
	private Runnable displayGui;

	public ForgeTeamPlayerJoinedEvent(ForgePlayer player)
	{
		super(player);
	}

	public void setDisplayGui(Runnable gui)
	{
		displayGui = gui;
	}

	@Nullable
	public Runnable getDisplayGui()
	{
		return displayGui;
	}
}