package com.feed_the_beast.ftblib.command.team;

import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.command.CommandUtils;
import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class CmdSettingsFor extends CmdBase
{
	public CmdSettingsFor()
	{
		super("settings_for", Level.OP);
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		if (args.length == 1)
		{
			List<String> list = new ArrayList<>();

			for (ForgeTeam team : Universe.get().getTeams())
			{
				if (team.type.isServer)
				{
					list.add(team.getID());
				}
			}

			return getListOfStringsMatchingLastWord(args, list);
		}

		return super.getTabCompletions(server, sender, args, pos);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		checkArgs(sender, args, 1);
		ForgeTeam team = CommandUtils.getTeam(sender, args[0]);

		if (team.type.isServer)
		{
			FTBLibAPI.editServerConfig(getCommandSenderAsPlayer(sender), team.getSettings(), team);
		}
	}
}