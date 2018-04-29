package com.feed_the_beast.ftblib.commands.team;

import com.feed_the_beast.ftblib.FTBLibLang;
import com.feed_the_beast.ftblib.lib.EnumTeamStatus;
import com.feed_the_beast.ftblib.lib.cmd.CmdBase;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
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

		if (!p.hasTeam())
		{
			throw FTBLibLang.TEAM_NO_TEAM.commandError();
		}
		else if (!p.team.isOwner(p))
		{
			throw FTBLibLang.TEAM_NOT_OWNER.commandError();
		}

		checkArgs(sender, args, 1);

		ForgePlayer p1 = getForgePlayer(sender, args[0]);

		if (!p.team.equalsTeam(p1.team))
		{
			throw FTBLibLang.TEAM_NOT_MEMBER.commandError(p1.getName());
		}

		p.team.setStatus(p1, EnumTeamStatus.OWNER);
	}
}
