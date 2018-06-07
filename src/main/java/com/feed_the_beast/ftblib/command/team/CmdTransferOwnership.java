package com.feed_the_beast.ftblib.command.team;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.EnumTeamStatus;
import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.command.CommandUtils;
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
	public boolean isUsernameIndex(String[] args, int index)
	{
		return index == 0;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		ForgePlayer p = CommandUtils.getForgePlayer(getCommandSenderAsPlayer(sender));

		if (!p.hasTeam())
		{
			throw FTBLib.error(sender, "ftblib.lang.team.error.no_team");
		}
		else if (!p.team.isOwner(p))
		{
			throw FTBLib.error(sender, "ftblib.lang.team.error.not_owner");
		}

		checkArgs(sender, args, 1);

		ForgePlayer p1 = CommandUtils.getForgePlayer(sender, args[0]);

		if (!p.team.equalsTeam(p1.team))
		{
			throw FTBLib.error(sender, "ftblib.lang.team.error.not_member", p1.getDisplayName());
		}

		p.team.setStatus(p1, EnumTeamStatus.OWNER);
	}
}
