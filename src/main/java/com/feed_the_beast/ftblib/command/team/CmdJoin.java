package com.feed_the_beast.ftblib.command.team;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.FTBLibGameRules;
import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.command.CommandUtils;
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
				ForgePlayer player = CommandUtils.getForgePlayer(sender);

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
			throw FTBLib.error(sender, "feature_disabled_server");
		}

		EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		ForgePlayer p = CommandUtils.getForgePlayer(player);

		if (p.hasTeam())
		{
			throw FTBLib.error(sender, "ftblib.lang.team.error.must_leave");
		}

		checkArgs(sender, args, 1);

		ForgeTeam team = CommandUtils.getTeam(sender, args[0]);

		if (!team.addMember(p, false))
		{
			throw FTBLib.error(sender, "ftblib.lang.team.error.not_member", p.getDisplayName());
		}
	}
}
