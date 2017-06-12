package com.feed_the_beast.ftbl.api.events.team;

import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.config.ConfigKey;

/**
 * @author LatvianModder
 */
public class ForgeTeamSettingsEvent extends ForgeTeamEvent
{
	private final IConfigTree settings;

	public ForgeTeamSettingsEvent(IForgeTeam team, IConfigTree tree)
	{
		super(team);
		settings = tree;
	}

	public IConfigKey add(String group, String id, IConfigValue value)
	{
		ConfigKey key = new ConfigKey(id, value, group, "team_config");
		settings.add(key, value);
		return key;
	}
}