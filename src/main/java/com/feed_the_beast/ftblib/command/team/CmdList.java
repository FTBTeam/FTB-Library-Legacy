package com.feed_the_beast.ftblib.command.team;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * @author LatvianModder
 */
public class CmdList extends CmdBase
{
	public CmdList()
	{
		super("list", Level.ALL);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		sender.sendMessage(FTBLib.lang(sender, "commands.team.list.teams", Universe.get().getTeams().size()));

		for (ForgeTeam team : Universe.get().getTeams())
		{
			sender.sendMessage(team.getCommandTitle());
		}
	}
}