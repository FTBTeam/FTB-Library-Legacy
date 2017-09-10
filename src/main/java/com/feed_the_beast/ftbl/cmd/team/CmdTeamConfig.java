package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.lib.cmd.CmdEditConfigBase;
import com.feed_the_beast.ftbl.lib.config.ConfigGroup;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * @author LatvianModder
 */
public class CmdTeamConfig extends CmdEditConfigBase
{
	public CmdTeamConfig()
	{
		super("config", Level.ALL);
	}

	private IForgeTeam getTeam(ICommandSender sender) throws CommandException
	{
		EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
		IForgePlayer p = getForgePlayer(ep);
		IForgeTeam team = p.getTeam();

		if (team == null)
		{
			throw FTBLibLang.TEAM_NO_TEAM.commandError();
		}
		else if (!team.hasStatus(p, EnumTeamStatus.MOD))
		{
			throw FTBLibLang.COMMAND_PERMISSION.commandError();
		}

		return team;
	}

	@Override
	public ConfigGroup getGroup(ICommandSender sender) throws CommandException
	{
		return getTeam(sender).getSettings();
	}
}