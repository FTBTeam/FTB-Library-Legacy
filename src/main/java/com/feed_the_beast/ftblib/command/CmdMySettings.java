package com.feed_the_beast.ftblib.command;

import com.feed_the_beast.ftblib.lib.command.CmdEditConfigBase;
import com.feed_the_beast.ftblib.lib.command.CommandUtils;
import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.config.IConfigCallback;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

/**
 * @author LatvianModder
 */
public class CmdMySettings extends CmdEditConfigBase
{
	public CmdMySettings()
	{
		super("my_settings", Level.ALL);
	}

	@Override
	public ConfigGroup getGroup(ICommandSender sender) throws CommandException
	{
		return CommandUtils.getForgePlayer(sender).getSettings();
	}

	@Override
	public IConfigCallback getCallback(ICommandSender sender) throws CommandException
	{
		return CommandUtils.getForgePlayer(sender);
	}
}