package com.feed_the_beast.ftblib.events.team;

import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;

/**
 * @author LatvianModder
 */
public class ForgeTeamConfigEvent extends ForgeTeamEvent
{
	private final ConfigGroup config;

	public ForgeTeamConfigEvent(ForgeTeam team, ConfigGroup s)
	{
		super(team);
		config = s;
	}

	public ConfigGroup getConfig()
	{
		return config;
	}
}