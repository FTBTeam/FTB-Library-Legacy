package com.feed_the_beast.ftblib.commands.team;

import com.feed_the_beast.ftblib.lib.cmd.CmdBase;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * @author LatvianModder
 */
public class CmdLeave extends CmdBase
{
	public CmdLeave()
	{
		super("leave", Level.ALL);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		ForgePlayer p = getForgePlayer(getCommandSenderAsPlayer(sender));

		if (!p.hasTeam())
		{
			throw new CommandException("ftblib.lang.team.error.no_team");
		}
		else if (!p.team.removeMember(p))
		{
			throw new CommandException("ftblib.lang.team.error.must_transfer_ownership");
		}
	}
}