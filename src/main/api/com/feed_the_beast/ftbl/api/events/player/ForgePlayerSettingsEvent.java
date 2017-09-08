package com.feed_the_beast.ftbl.api.events.player;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.lib.config.AdvancedConfigKey;
import com.feed_the_beast.ftbl.lib.config.ConfigTree;
import com.feed_the_beast.ftbl.lib.config.ConfigValue;

/**
 * @author LatvianModder
 */
public class ForgePlayerSettingsEvent extends ForgePlayerEvent
{
	private final ConfigTree settings;

	public ForgePlayerSettingsEvent(IForgePlayer player, ConfigTree tree)
	{
		super(player);
		settings = tree;
	}

	public AdvancedConfigKey add(String group, String id, ConfigValue value)
	{
		AdvancedConfigKey key = new AdvancedConfigKey(id, value, group);
		settings.add(key, value);
		return key;
	}
}