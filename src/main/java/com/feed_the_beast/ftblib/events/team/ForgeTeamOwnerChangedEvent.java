package com.feed_the_beast.ftblib.events.team;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class ForgeTeamOwnerChangedEvent extends ForgeTeamEvent
{
	private final ForgePlayer oldOwner;

	public ForgeTeamOwnerChangedEvent(ForgeTeam team, @Nullable ForgePlayer o0)
	{
		super(team);
		oldOwner = o0;
	}

	@Nullable
	public ForgePlayer getOldOwner()
	{
		return oldOwner;
	}
}