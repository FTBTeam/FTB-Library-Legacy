package com.feed_the_beast.ftbl.api.events.player;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.lib.config.ConfigGroup;

/**
 * @author LatvianModder
 */
public class ForgePlayerConfigEvent extends ForgePlayerEvent
{
	private final ConfigGroup config;

	public ForgePlayerConfigEvent(IForgePlayer player, ConfigGroup s)
	{
		super(player);
		config = s;
	}

	public ConfigGroup getConfig()
	{
		return config;
	}
}