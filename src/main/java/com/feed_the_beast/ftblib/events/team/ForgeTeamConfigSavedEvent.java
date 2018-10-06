package com.feed_the_beast.ftblib.events.team;

import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import net.minecraft.command.ICommandSender;

/**
 * @author LatvianModder
 */
public class ForgeTeamConfigSavedEvent extends ForgeTeamEvent
{
	private final ConfigGroup config;
	private final ICommandSender sender;

	public ForgeTeamConfigSavedEvent(ForgeTeam team, ConfigGroup s, ICommandSender ics)
	{
		super(team);
		config = s;
		sender = ics;
	}

	public ConfigGroup getConfig()
	{
		return config;
	}

	public ICommandSender getSender()
	{
		return sender;
	}
}