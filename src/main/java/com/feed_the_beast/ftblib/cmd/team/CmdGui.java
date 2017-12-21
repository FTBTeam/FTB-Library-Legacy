package com.feed_the_beast.ftblib.cmd.team;

import com.feed_the_beast.ftblib.lib.cmd.CmdBase;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.net.MessageMyTeamGui;
import com.feed_the_beast.ftblib.net.MessageSelectTeamGui;
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
		ForgePlayer p = getForgePlayer(player);
		ForgeTeam team = p.getTeam();

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