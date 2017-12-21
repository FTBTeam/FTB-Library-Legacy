package com.feed_the_beast.ftblib.cmd.team;

import com.feed_the_beast.ftblib.FTBLibLang;
import com.feed_the_beast.ftblib.events.team.ForgeTeamCreatedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftblib.lib.EnumTeamColor;
import com.feed_the_beast.ftblib.lib.EnumTeamStatus;
import com.feed_the_beast.ftblib.lib.cmd.CmdBase;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.net.MessageMyTeamGui;
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
		EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		ForgePlayer p = getForgePlayer(player);

		if (p.getTeam() != null)
		{
			throw FTBLibLang.TEAM_MUST_LEAVE.commandError();
		}

		checkArgs(sender, args, 1);

		if (!isValidTeamID(args[0]))
		{
			throw FTBLibLang.TEAM_ID_INVALID.commandError();
		}

		if (Universe.get().getTeam(args[0]) != null)
		{
			throw FTBLibLang.TEAM_ID_ALREADY_EXISTS.commandError();
		}

		ForgeTeam team = new ForgeTeam(args[0]);

		if (args.length > 1)
		{
			team.setColor(EnumTeamColor.NAME_MAP.get(args[1]));
		}

		Universe.get().teams.put(team.getName(), team);
		p.setTeamId(team.getName());
		team.setStatus(p, EnumTeamStatus.OWNER);

		new ForgeTeamCreatedEvent(team).post();
		new ForgeTeamPlayerJoinedEvent(team, p).post();

		FTBLibLang.TEAM_CREATED.sendMessage(sender, team.getName());
		new MessageMyTeamGui(team, p).sendTo(player);
	}
}