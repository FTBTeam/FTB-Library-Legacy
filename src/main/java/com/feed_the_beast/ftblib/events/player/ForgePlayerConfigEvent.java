package com.feed_the_beast.ftblib.events.player;

import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;

/**
 * @author LatvianModder
 */
public class ForgePlayerConfigEvent extends ForgePlayerEvent
{
	private final ConfigGroup config;

	public ForgePlayerConfigEvent(ForgePlayer player, ConfigGroup s)
	{
		super(player);
		config = s;
	}

	public ConfigGroup getConfig()
	{
		return config;
	}
}