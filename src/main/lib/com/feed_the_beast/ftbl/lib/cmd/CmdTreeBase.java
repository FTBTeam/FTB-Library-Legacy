package com.feed_the_beast.ftbl.lib.cmd;

import com.feed_the_beast.ftbl.api.ICustomPermission;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;

/**
 * @author LatvianModder
 */
public class CmdTreeBase extends CommandTreeBase implements ICustomPermission
{
	private final String name;
	private CmdBase.Level level = CmdBase.Level.OP;
	private String customPermission;
	private String usage;

	public CmdTreeBase(String n)
	{
		name = n;
		setCustomPermissionPrefix("");
	}

	@Override
	public void addSubcommand(ICommand command)
	{
		super.addSubcommand(command);

		if (command instanceof CmdBase && ((CmdBase) command).level == CmdBase.Level.ALL)
		{
			level = CmdBase.Level.ALL;
		}

		if (command instanceof CmdTreeBase && ((CmdTreeBase) command).level == CmdBase.Level.ALL)
		{
			level = CmdBase.Level.ALL;
		}
	}

	public void setLevel(CmdBase.Level l)
	{
		level = l;
	}

	@Override
	public final String getName()
	{
		return name;
	}

	@Override
	public final String getCustomPermission()
	{
		return customPermission;
	}

	@Override
	public final int getRequiredPermissionLevel()
	{
		return level.level;
	}

	@Override
	public final String getUsage(ICommandSender ics)
	{
		return usage;
	}

	@Override
	public final boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return level.checkPermission(sender, this);
	}

	@Override
	public void setCustomPermissionPrefix(String prefix)
	{
		String s = prefix.isEmpty() ? name : (prefix + "." + name);
		customPermission = "command." + s;
		usage = "commands." + s + ".usage";

		for (ICommand command : getSubCommands())
		{
			if (command instanceof ICustomPermission)
			{
				((ICustomPermission) command).setCustomPermissionPrefix(s);
			}
		}
	}
}