package com.feed_the_beast.ftblib.commands;

import com.feed_the_beast.ftblib.lib.cmd.CmdBase;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.TextComponentHelper;

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
	public boolean isUsernameIndex(String[] args, int i)
	{
		return i == 0;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		checkArgs(sender, args, 2);

		UUID id = StringUtils.fromString(args[0]);

		if (id == null)
		{
			throw new CommandException("ftblib.lang.add_fake_player.invalid_uuid");
		}

		if (Universe.get().getPlayer(id) != null || Universe.get().getPlayer(args[1]) != null)
		{
			throw new CommandException("ftblib.lang.add_fake_player.player_exists");
		}

		ForgePlayer p = new ForgePlayer(Universe.get(), id, args[1]);
		p.team.universe.players.put(p.getId(), p);
		sender.sendMessage(TextComponentHelper.createComponentTranslation(sender, "ftblib.lang.add_fake_player.added", p.getName()));
	}
}
