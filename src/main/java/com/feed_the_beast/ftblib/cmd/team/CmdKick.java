package com.feed_the_beast.ftblib.cmd.team;

import com.feed_the_beast.ftblib.FTBLibLang;
import com.feed_the_beast.ftblib.lib.cmd.CmdBase;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * @author LatvianModder
 */
public class CmdKick extends CmdBase
{
	public CmdKick()
	{
		super("kick", Level.ALL);
	}

	@Override
	public boolean isUsernameIndex(String[] args, int i)
	{
		return i == 0;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		ForgePlayer p = getForgePlayer(getCommandSenderAsPlayer(sender));
		ForgeTeam team = p.getTeam();

		if (team == null)
		{
			throw FTBLibLang.TEAM_NO_TEAM.commandError();
		}
		else if (!team.isModerator(p))
		{
			throw FTBLibLang.COMMAND_PERMISSION.commandError();
		}

		checkArgs(sender, args, 1);

		ForgePlayer p1 = getForgePlayer(sender, args[0]);

		if (!team.isMember(p1))
		{
			throw FTBLibLang.TEAM_NOT_MEMBER.commandError(p1.getName());
		}
		else if (!p1.equalsPlayer(p))
		{
			team.removeMember(p1);
		}
		else
		{
			throw FTBLibLang.TEAM_MUST_TRANSFER_OWNERSHIP.commandError();
		}
	}
}
