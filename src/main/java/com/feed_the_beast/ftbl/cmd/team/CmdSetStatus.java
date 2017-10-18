package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.lib.cmd.CmdBase;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class CmdSetStatus extends CmdBase
{
	public CmdSetStatus()
	{
		super("set_status", Level.ALL);
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		if (args.length == 2)
		{
			return getListOfStringsMatchingLastWord(args, EnumTeamStatus.VALID_VALUES);
		}

		return super.getTabCompletions(server, sender, args, pos);
	}

	@Override
	public boolean isUsernameIndex(String[] args, int i)
	{
		return i == 0;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		IForgePlayer p = getForgePlayer(getCommandSenderAsPlayer(sender));
		IForgeTeam team = p.getTeam();

		if (team == null)
		{
			throw FTBLibLang.TEAM_NO_TEAM.commandError();
		}
		else if (!team.isModerator(p))
		{
			throw FTBLibLang.COMMAND_PERMISSION.commandError();
		}

		checkArgs(sender, args, 2);
		IForgePlayer p1 = getForgePlayer(args[0]);

		if (team.isOwner(p1))
		{
			throw FTBLibLang.TEAM_PERMISSION_OWNER.commandError();
		}
		else if (!team.isModerator(p))
		{
			throw FTBLibLang.COMMAND_PERMISSION.commandError();
		}

		EnumTeamStatus status = EnumTeamStatus.NAME_MAP.get(args[1].toLowerCase());

		if (status.canBeSet())
		{
			team.setStatus(p1, status);
		}

		//TODO: Display notification
	}
}
