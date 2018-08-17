package com.feed_the_beast.ftblib.command.team;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.events.team.ForgeTeamCreatedEvent;
import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.TeamType;
import com.feed_the_beast.ftblib.lib.data.Universe;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * @author LatvianModder
 */
public class CmdCreateServerTeam extends CmdBase
{
	public CmdCreateServerTeam()
	{
		super("create_server_team", Level.OP_OR_SP);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		checkArgs(sender, args, 1);

		if (!CmdCreate.isValidTeamID(args[0]))
		{
			throw FTBLib.error(sender, "ftblib.lang.team.id_invalid");
		}

		if (Universe.get().getTeam(args[0]).isValid())
		{
			throw FTBLib.error(sender, "ftblib.lang.team.id_already_exists");
		}

		Universe.get().clearCache();
		ForgeTeam team = new ForgeTeam(Universe.get(), args[0], TeamType.SERVER);
		team.setTitle(team.getName());
		team.universe.teams.put(team.getName(), team);
		new ForgeTeamCreatedEvent(team).post();
		sender.sendMessage(FTBLib.lang(sender, "ftblib.lang.team.created", team.getName()));
		team.markDirty();
	}
}