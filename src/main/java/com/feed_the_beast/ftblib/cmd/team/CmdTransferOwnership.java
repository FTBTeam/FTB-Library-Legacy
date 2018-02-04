package com.feed_the_beast.ftblib.cmd.team;

import com.feed_the_beast.ftblib.FTBLibLang;
import com.feed_the_beast.ftblib.lib.EnumTeamStatus;
import com.feed_the_beast.ftblib.lib.cmd.CmdBase;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * @author LatvianModder
 */
public class CmdTransferOwnership extends CmdBase
{
	public CmdTransferOwnership()
	{
		super("transfer_ownership", Level.ALL);
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
		else if (!team.isOwner(p))
		{
			throw FTBLibLang.TEAM_NOT_OWNER.commandError();
		}

		checkArgs(sender, args, 1);

		ForgePlayer p1 = getForgePlayer(sender, args[0]);

		if (!team.equals(p1.getTeam()))
		{
			throw FTBLibLang.TEAM_NOT_MEMBER.commandError(p1.getName());
		}

		team.setStatus(p1, EnumTeamStatus.OWNER);
	}
}
