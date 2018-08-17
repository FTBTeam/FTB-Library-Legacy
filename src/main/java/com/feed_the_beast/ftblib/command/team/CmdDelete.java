package com.feed_the_beast.ftblib.command.team;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class CmdDelete extends CmdBase
{
	public CmdDelete()
	{
		super("delete", Level.OP_OR_SP);
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		if (args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args, Universe.get().getTeams());
		}

		return super.getTabCompletions(server, sender, args, pos);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		checkArgs(sender, args, 1);

		ForgeTeam team = Universe.get().getTeam(args[0]);

		if (!team.isValid())
		{
			throw FTBLib.error(sender, "ftblib.lang.team.error.not_found", args[0]);
		}

		for (ForgePlayer player : team.getMembers())
		{
			if (player != team.owner)
			{
				team.removeMember(player);
			}
		}

		if (team.owner != null)
		{
			team.removeMember(team.owner);
		}

		team.delete();
	}
}