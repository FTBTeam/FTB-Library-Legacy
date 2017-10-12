package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
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

	@Override
	public ConfigGroup getGroup(ICommandSender sender) throws CommandException
	{
		EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
		IForgePlayer p = getForgePlayer(ep);
		IForgeTeam team = p.getTeam();

		if (team == null)
		{
			FTBLibAPI.API.sendCloseGuiPacket(ep);
			throw FTBLibLang.TEAM_NO_TEAM.commandError();
		}
		else if (!team.hasStatus(p, EnumTeamStatus.MOD))
		{
			FTBLibAPI.API.sendCloseGuiPacket(ep);
			throw FTBLibLang.COMMAND_PERMISSION.commandError();
		}

		return team.getSettings();
	}
}