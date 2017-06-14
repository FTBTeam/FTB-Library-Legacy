package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.feed_the_beast.ftbl.lib.cmd.CmdBase;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
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
	public boolean isUsernameIndex(String[] args, int i)
	{
		return i == 0;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		checkArgs(args, 2, "<uuid> <name>");

		UUID id = StringUtils.fromString(args[0]);
		if (id == null)
		{
			throw FTBLibLang.RAW.commandError("Invalid UUID!");
		}

		if (Universe.INSTANCE.getPlayer(id) != null || Universe.INSTANCE.getPlayer(args[1]) != null)
		{
			throw FTBLibLang.RAW.commandError("Player already exists!");
		}

		ForgePlayer p = new ForgePlayer(id, args[1]);
		Universe.INSTANCE.playerMap.put(p.getId(), p);

		sender.sendMessage(StringUtils.text("Fake player " + args[1] + " added!"));
	}
}
