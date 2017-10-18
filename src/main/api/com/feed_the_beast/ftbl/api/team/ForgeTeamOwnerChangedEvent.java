package com.feed_the_beast.ftbl.api.team;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class ForgeTeamOwnerChangedEvent extends ForgeTeamEvent
{
	private final IForgePlayer oldOwner;

	public ForgeTeamOwnerChangedEvent(IForgeTeam team, @Nullable IForgePlayer o0)
	{
		super(team);
		oldOwner = o0;
	}

	@Nullable
	public IForgePlayer getOldOwner()
	{
		return oldOwner;
	}
}