package com.feed_the_beast.ftbl.api.events.team;

import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.lib.config.ConfigGroup;

/**
 * @author LatvianModder
 */
public class ForgeTeamConfigEvent extends ForgeTeamEvent
{
	private final ConfigGroup config;

	public ForgeTeamConfigEvent(IForgeTeam team, ConfigGroup s)
	{
		super(team);
		config = s;
	}

	public ConfigGroup getConfig()
	{
		return config;
	}
}