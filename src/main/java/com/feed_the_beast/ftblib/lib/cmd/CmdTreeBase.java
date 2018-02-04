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
	private int level = 4;
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

		if (command instanceof CommandBase)
		{
			level = Math.min(level, ((CommandBase) command).getRequiredPermissionLevel());
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
		return level <= 0 || sender.canUseCommand(level, getName());
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