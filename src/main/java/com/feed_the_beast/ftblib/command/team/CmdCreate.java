package com.feed_the_beast.ftblib.command.team;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.FTBLibGameRules;
import com.feed_the_beast.ftblib.events.team.ForgeTeamCreatedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftblib.lib.EnumTeamColor;
import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.command.CommandUtils;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.TeamType;
import com.feed_the_beast.ftblib.net.MessageMyTeamGuiResponse;
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

	public static boolean isValidTeamID(String s)
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
		if (!FTBLibGameRules.canCreateTeam(server.getWorld(0)))
		{
			throw FTBLib.error(sender, "feature_disabled_server");
		}

		EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		ForgePlayer p = CommandUtils.getForgePlayer(player);

		if (p.hasTeam())
		{
			throw FTBLib.error(sender, "ftblib.lang.team.error.must_leave");
		}

		checkArgs(sender, args, 1);

		if (!isValidTeamID(args[0]))
		{
			throw FTBLib.error(sender, "ftblib.lang.team.id_invalid");
		}

		if (p.team.universe.getTeam(args[0]).isValid())
		{
			throw FTBLib.error(sender, "ftblib.lang.team.id_already_exists");
		}

		p.team.universe.clearCache();

		ForgeTeam team = new ForgeTeam(p.team.universe, p.team.universe.generateTeamUID((short) 0), args[0], TeamType.PLAYER);

		if (args.length > 1)
		{
			team.setColor(EnumTeamColor.NAME_MAP.get(args[1]));
		}
		else
		{
			team.setColor(EnumTeamColor.NAME_MAP.getRandom(sender.getEntityWorld().rand));
		}

		p.team = team;
		team.owner = p;
		team.universe.addTeam(team);
		new ForgeTeamCreatedEvent(team).post();
		new ForgeTeamPlayerJoinedEvent(p).post();
		sender.sendMessage(FTBLib.lang(sender, "ftblib.lang.team.created", team.getID()));
		new MessageMyTeamGuiResponse(p).sendTo(player);
		team.markDirty();
		p.markDirty();
	}
}