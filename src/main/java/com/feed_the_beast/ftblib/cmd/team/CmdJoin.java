package com.feed_the_beast.ftblib.cmd.team;

import com.feed_the_beast.ftblib.FTBLibGameRules;
import com.feed_the_beast.ftblib.FTBLibLang;
import com.feed_the_beast.ftblib.lib.cmd.CmdBase;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class CmdJoin extends CmdBase
{
	public CmdJoin()
	{
		super("join", Level.ALL);
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		if (args.length == 1)
		{
			if (!FTBLibGameRules.canJoinTeam(server.getWorld(0)))
			{
				return Collections.emptyList();
			}

			List<String> list = new ArrayList<>();

			try
			{
				ForgePlayer player = getForgePlayer(sender);

				for (ForgeTeam team : Universe.get().getTeams())
				{
					if (team.addMember(player, true))
					{
						list.add(team.getName());
					}
				}

				if (list.size() > 1)
				{
					list.sort(null);
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}

			return getListOfStringsMatchingLastWord(args, list);
		}

		return super.getTabCompletions(server, sender, args, pos);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (!FTBLibGameRules.canJoinTeam(server.getWorld(0)))
		{
			throw FTBLibLang.FEATURE_DISABLED.commandError();
		}

		EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		ForgePlayer p = getForgePlayer(player);

		if (p.hasTeam())
		{
			throw FTBLibLang.TEAM_MUST_LEAVE.commandError();
		}

		checkArgs(sender, args, 1);

		ForgeTeam team = getTeam(args[0]);

		if (!team.addMember(p, false))
		{
			throw FTBLibLang.TEAM_NOT_MEMBER.commandError(p.getName());
		}
	}
}
