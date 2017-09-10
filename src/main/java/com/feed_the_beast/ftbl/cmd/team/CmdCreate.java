package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.api.EnumTeamColor;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamCreatedEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.feed_the_beast.ftbl.lib.cmd.CmdBase;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

/**
 * @author LatvianModder
 */
public class CmdCreate extends CmdBase
{
	public CmdCreate()
	{
		super("create", Level.ALL);
	}

	private static boolean isValidTeamID(String s)
	{
		if (!s.isEmpty())
		{
			for (int i = 0; i < s.length(); i++)
			{
				if (!isValidChar(s.charAt(i)))
				{
					return false;
				}
			}

			return true;
		}

		return false;
	}

	private static boolean isValidChar(char c)
	{
		return c == '_' || c == '|' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9');
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
		IForgePlayer p = getForgePlayer(ep);

		if (p.getTeam() != null)
		{
			throw FTBLibLang.TEAM_MUST_LEAVE.commandError();
		}

		checkArgs(args, 1, "<id> [color]");

		if (!isValidTeamID(args[0]))
		{
			throw FTBLibLang.RAW.commandError("ID can only contain lowercase a-z, _ and |!");
		}

		if (Universe.INSTANCE.getTeam(args[0]) != null)
		{
			throw FTBLibLang.RAW.commandError("ID already registred!");
		}

		ForgeTeam team = new ForgeTeam(args[0], p);

		if (args.length > 1)
		{
			team.setColor(EnumTeamColor.NAME_MAP.get(args[1]));
		}

		Universe.INSTANCE.teams.put(team.getName(), team);
		team.changeOwner(p);

		new ForgeTeamCreatedEvent(team).post();
		new ForgeTeamPlayerJoinedEvent(team, p).post();

		FTBLibLang.TEAM_CREATED.printChat(sender, team.getName());
	}
}