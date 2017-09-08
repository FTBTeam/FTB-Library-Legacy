package com.feed_the_beast.ftbl.api.events.team;

import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.lib.config.AdvancedConfigKey;
import com.feed_the_beast.ftbl.lib.config.ConfigTree;
import com.feed_the_beast.ftbl.lib.config.ConfigValue;

/**
 * @author LatvianModder
 */
public class ForgeTeamSettingsEvent extends ForgeTeamEvent
{
	private final ConfigTree settings;

	public ForgeTeamSettingsEvent(IForgeTeam team, ConfigTree tree)
	{
		super(team);
		settings = tree;
	}

	public AdvancedConfigKey add(String group, String id, ConfigValue value)
	{
		AdvancedConfigKey key = new AdvancedConfigKey(id, value, group);
		settings.add(key, value);
		return key;
	}
}