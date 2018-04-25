package com.feed_the_beast.ftblib.lib.cmd;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class CmdTreeBase extends CommandTreeBase implements ICommandWithParent
{
	private final String name;
	private ICommand parent;

	public CmdTreeBase(String n)
	{
		name = n;
	}

	@Override
	public void addSubcommand(ICommand command)
	{
		super.addSubcommand(command);

		if (command instanceof ICommandWithParent)
		{
			((ICommandWithParent) command).setParent(this);
		}
	}

	@Override
	public final String getName()
	{
		return name;
	}

	@Override
	public final int getRequiredPermissionLevel()
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
		return "commands." + ICommandWithParent.getFullPath(this) + ".usage";
	}

	@Override
	public final boolean checkPermission(MinecraftServer server, ICommandSender sender)
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

	@Override
	public void setParent(@Nullable ICommand p)
	{
		parent = p;
	}

	@Override
	@Nullable
	public ICommand getParent()
	{
		return parent;
	}
}