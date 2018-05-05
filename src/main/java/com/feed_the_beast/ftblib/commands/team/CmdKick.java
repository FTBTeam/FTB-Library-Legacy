package com.feed_the_beast.ftblib.commands.team;

import com.feed_the_beast.ftblib.lib.cmd.CmdBase;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * @author LatvianModder
 */
public class CmdKick extends CmdBase
{
	public CmdKick()
	{
		super("kick", Level.ALL);
	}

	@Override
	public boolean isUsernameIndex(String[] args, int i)
	{
		return i == 0;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		ForgePlayer p = getForgePlayer(getCommandSenderAsPlayer(sender));

		if (!p.hasTeam())
		{
			throw new CommandException("ftblib.lang.team.error.no_team");
		}
		else if (!p.team.isModerator(p))
		{
			throw new CommandException("commands.generic.permission");
		}

		checkArgs(sender, args, 1);

		ForgePlayer p1 = getForgePlayer(sender, args[0]);

		if (!p.team.isMember(p1))
		{
			throw new CommandException("ftblib.lang.team.error.not_member", p1.getName());
		}
		else if (!p1.equalsPlayer(p))
		{
			p.team.removeMember(p1);
		}
		else
		{
			throw new CommandException("ftblib.lang.team.error.must_transfer_ownership");
		}
	}
}
