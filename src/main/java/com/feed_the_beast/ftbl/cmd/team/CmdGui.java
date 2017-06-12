package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.feed_the_beast.ftbl.client.teamsgui.MyTeamPlayerData;
import com.feed_the_beast.ftbl.lib.cmd.CmdBase;
import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.feed_the_beast.ftbl.net.MessageMyTeamAddPlayerGui;
import com.feed_the_beast.ftbl.net.MessageMyTeamGui;
import com.feed_the_beast.ftbl.net.MessageSelectTeamGui;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class CmdGui extends CmdBase
{
	public CmdGui()
	{
		super("gui", Level.ALL);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		IUniverse universe = FTBLibIntegrationInternal.API.getUniverse();

		if (universe == null)
		{
			return;
		}

		EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		IForgePlayer p = getForgePlayer(player);
		IForgeTeam team = p.getTeam();

		if (team != null)
		{
			if (args.length >= 1 && args[0].equals("add_player"))
			{
				if (!team.hasStatus(p, EnumTeamStatus.MOD))
				{
					throw FTBLibLang.COMMAND_PERMISSION.commandError();
				}

				if (args.length >= 3)
				{
					UUID id = StringUtils.fromString(args[1]);
					EnumTeamStatus status = EnumTeamStatus.NAME_MAP.get(args[2]);

					if (id != null && status != null && status.canBeSet() && !team.hasStatus(id, EnumTeamStatus.MEMBER) && (!status.isEqualOrGreaterThan(EnumTeamStatus.MOD) || team.hasStatus(p, EnumTeamStatus.OWNER)))
					{
						team.setStatus(id, status);
					}
				}
				else
				{
					Collection<MyTeamPlayerData> players = new ArrayList<>();

					for (IForgePlayer player1 : Universe.INSTANCE.getPlayers())
					{
						players.add(new MyTeamPlayerData(player1, team.getHighestStatus(player1)));
					}

					new MessageMyTeamAddPlayerGui(players).sendTo(player);
				}
			}
			else
			{
				new MessageMyTeamGui(universe, team, p).sendTo(player);
			}
		}
		else
		{
			new MessageSelectTeamGui(universe, p).sendTo(player);
		}
	}
}