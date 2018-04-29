package com.feed_the_beast.ftblib.commands.team;

import com.feed_the_beast.ftblib.FTBLibGameRules;
import com.feed_the_beast.ftblib.FTBLibLang;
import com.feed_the_beast.ftblib.lib.cmd.CmdBase;
import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.net.MessageMyTeamGuiResponse;
import com.feed_the_beast.ftblib.net.MessageSelectTeamGui;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

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
		World world = server.getWorld(0);
		if (!FTBLibGameRules.canCreateTeam(world) && !FTBLibGameRules.canJoinTeam(world))
		{
			FTBLibAPI.sendCloseGuiPacket(getCommandSenderAsPlayer(sender));
			throw FTBLibLang.FEATURE_DISABLED_SERVER.commandError();
		}

		EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		ForgePlayer p = getForgePlayer(player);
		(p.hasTeam() ? new MessageMyTeamGuiResponse(p) : new MessageSelectTeamGui(p, FTBLibGameRules.canCreateTeam(world))).sendTo(player);
	}
}