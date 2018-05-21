package com.feed_the_beast.ftblib.events.team;

import com.feed_the_beast.ftblib.lib.data.ForgeTeam;

import java.io.File;

/**
 * @author LatvianModder
 */
public class ForgeTeamDeletedEvent extends ForgeTeamEvent
{
	private final File folder;

	public ForgeTeamDeletedEvent(ForgeTeam team, File f)
	{
		super(team);
		folder = f;
	}

	public File getDataFolder()
	{
		return folder;
	}
}