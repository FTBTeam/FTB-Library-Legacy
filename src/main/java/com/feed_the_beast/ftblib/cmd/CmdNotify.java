package com.feed_the_beast.ftblib.cmd;

import com.feed_the_beast.ftblib.lib.cmd.CmdBase;
import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.feed_the_beast.ftblib.lib.util.ServerUtils;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

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
			return getListOfStringsMatchingLastWord(args, "{text:\"Hi!\",id:\"minecraft:test\"}");
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
		checkArgs(sender, args, 2);
		EntityPlayerMP player = getPlayer(server, sender, args[0]);
		ITextComponent component = JsonUtils.deserializeTextComponent(JsonUtils.fromJson(String.join(" ", StringUtils.shiftArray(args))));
		ServerUtils.notify(server, player, Objects.requireNonNull(component));
	}
}