package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.lib.cmd.CmdBase;
import com.feed_the_beast.ftbl.net.MessageMyTeamGui;
import com.feed_the_beast.ftbl.net.MessageSelectTeamGui;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

/**
 * @author LatvianModder
 */
public class CmdGui extends CmdBase
{
	public CmdGui()
	{
		super("gui", Level.ALL);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		IForgePlayer p = getForgePlayer(player);
		IForgeTeam team = p.getTeam();

		if (team != null)
		{
			new MessageMyTeamGui(team, p).sendTo(player);
		}
		else
		{
			new MessageSelectTeamGui(p).sendTo(player);
		}
	}
}