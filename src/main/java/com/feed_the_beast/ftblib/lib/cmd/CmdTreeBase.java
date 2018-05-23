package com.feed_the_beast.ftblib.lib.cmd;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;

/**
 * @author LatvianModder
 */
public class CmdTreeBase extends CommandTreeBase
{
	private final String name;

	public CmdTreeBase(String n)
	{
		name = n;
	}

	@Override
	public final String getName()
	{
		return name;
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		int level = 0;

		for (ICommand command : getSubCommands())
		{
			if (command instanceof CommandBase)
			{
				level = Math.max(level, ((CommandBase) command).getRequiredPermissionLevel());
			}
		}

		return level;
	}

	@Override
	public String getUsage(ICommandSender ics)
	{
		return "commands." + getName() + ".usage";
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		for (ICommand command : getSubCommands())
		{
			if (command.checkPermission(server, sender))
			{
				return true;
			}
		}

		return false;
	}
}