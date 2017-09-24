package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.feed_the_beast.ftbl.lib.cmd.CmdBase;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class CmdRequestInvite extends CmdBase
{
	public CmdRequestInvite()
	{
		super("request_invite", Level.ALL);
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		if (args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args, EnumTeamStatus.VALID_VALUES);
		}

		return super.getTabCompletions(server, sender, args, pos);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
		IForgePlayer p = getForgePlayer(ep);

		if (p.getTeam() != null)
		{
			throw FTBLibLang.TEAM_MUST_LEAVE.commandError();
		}

		checkArgs(args, 1, "<team>");

		ForgeTeam team = Universe.INSTANCE.getTeam(args[0]);

		if (team == null)
		{
			throw FTBLibLang.ERROR.commandError(args[0]);
		}

		EnumTeamStatus status = team.getHighestStatus(p);

		if (status == EnumTeamStatus.NONE)
		{
			team.setStatus(p.getId(), EnumTeamStatus.REQUESTING_INVITE);
		}
	}
}