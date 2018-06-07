package com.feed_the_beast.ftblib.command;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.util.UUID;

/**
 * @author LatvianModder
 */
public class CmdAddFakePlayer extends CmdBase
{
	public CmdAddFakePlayer()
	{
		super("add_fake_player", Level.OP);
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index)
	{
		return index == 0;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		checkArgs(sender, args, 2);

		UUID id = StringUtils.fromString(args[0]);

		if (id == null)
		{
			throw FTBLib.error(sender, "ftblib.lang.add_fake_player.invalid_uuid");
		}

		if (Universe.get().getPlayer(id) != null || Universe.get().getPlayer(args[1]) != null)
		{
			throw FTBLib.error(sender, "ftblib.lang.add_fake_player.player_exists");
		}

		ForgePlayer p = new ForgePlayer(Universe.get(), id, args[1]);
		p.team.universe.players.put(p.getId(), p);
		p.clearCache();
		sender.sendMessage(FTBLib.lang(sender, "ftblib.lang.add_fake_player.added", p.getDisplayName()));
	}
}
