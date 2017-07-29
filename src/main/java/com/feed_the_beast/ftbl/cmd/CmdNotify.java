package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.lib.cmd.CmdBase;
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
public class CmdNotify extends CmdBase
{
	public CmdNotify()
	{
		super("notify", Level.OP);
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		if (args.length == 2)
		{
			return getListOfStringsMatchingLastWord(args, "{\"id\":\"minecraft:test\", \"text\":[\"Hi\"]}");
		}

		return super.getTabCompletions(server, sender, args, pos);
	}

	@Override
	public boolean isUsernameIndex(String[] args, int i)
	{
		return i == 0;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		checkArgs(args, 2, "<player> <id>");
		EntityPlayerMP player = getPlayer(server, sender, args[0]);

		if (args[1].startsWith("{") && args[1].endsWith("}"))
		{
			//TODO: Custom notification support
			//FTBLibAPI.API.sendNotification(player, n);
		}
	}
}