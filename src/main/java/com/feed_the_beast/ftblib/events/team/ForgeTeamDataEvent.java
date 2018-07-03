package com.feed_the_beast.ftblib.events.team;

import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.TeamData;

import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class ForgeTeamDataEvent extends ForgeTeamEvent
{
	private final Consumer<TeamData> callback;

	public ForgeTeamDataEvent(ForgeTeam team, Consumer<TeamData> c)
	{
		super(team);
		callback = c;
	}

	public void register(TeamData data)
	{
		callback.accept(data);
	}
}