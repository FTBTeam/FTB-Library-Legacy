package com.feed_the_beast.ftblib.cmd.team;

import com.feed_the_beast.ftblib.FTBLibLang;
import com.feed_the_beast.ftblib.lib.cmd.CmdEditConfigBase;
import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
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
		EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		ForgePlayer p = getForgePlayer(player);
		ForgeTeam team = p.getTeam();

		if (team == null)
		{
			FTBLibAPI.sendCloseGuiPacket(player);
			throw FTBLibLang.TEAM_NO_TEAM.commandError();
		}
		else if (!team.isModerator(p))
		{
			FTBLibAPI.sendCloseGuiPacket(player);
			throw FTBLibLang.COMMAND_PERMISSION.commandError();
		}

		return team.getSettings();
	}
}