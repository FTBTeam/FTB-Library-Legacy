package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.lib.cmd.CmdEditConfigBase;
import com.feed_the_beast.ftbl.lib.config.ConfigGroup;
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
		return getForgePlayer(sender).getSettings();
	}
}