package com.feed_the_beast.ftblib.events.player;

import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import net.minecraft.command.ICommandSender;

/**
 * @author LatvianModder
 */
public class ForgePlayerConfigSavedEvent extends ForgePlayerEvent
{
	private final ConfigGroup config;
	private final ICommandSender sender;

	public ForgePlayerConfigSavedEvent(ForgePlayer player, ConfigGroup s, ICommandSender ics)
	{
		super(player);
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