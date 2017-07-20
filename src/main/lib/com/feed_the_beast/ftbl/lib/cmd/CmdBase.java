package com.feed_the_beast.ftbl.lib.cmd;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.ICustomPermission;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class CmdBase extends CommandBase implements ICustomPermission
{
	public enum Level
	{
		ALL(0),
		OP(2);

		public final int level;

		Level(int l)
		{
			level = l;
		}

		public boolean checkPermission(ICommandSender sender, ICommand command)
		{
			return level <= 0 || sender.canUseCommand(level, command.getName());
		}
	}

	protected static final List<String> LIST_TRUE_FALSE = Collections.unmodifiableList(Arrays.asList("true", "false"));

	public static void checkArgs(String[] args, int i, String desc) throws CommandException
	{
		if (args.length < i)
		{
			if (desc.isEmpty())
			{
				throw FTBLibLang.MISSING_ARGS_NUM.commandError(Integer.toString(i - args.length));
			}
			else
			{
				throw FTBLibLang.MISSING_ARGS.commandError(desc);
			}
		}
	}

	private final String name;
	public final Level level;
	private String customPermission;
	private String usage;

	public CmdBase(String n, Level l)
	{
		name = n;
		level = l;
		setCustomPermissionPrefix("");
	}

	@Override
	public final String getName()
	{
		return name;
	}

	@Override
	public final String getCustomPermission()
	{
		return customPermission;
	}

	@Override
	public final int getRequiredPermissionLevel()
	{
		return level.level;
	}

	@Override
	public final String getUsage(ICommandSender ics)
	{
		return usage;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return level.checkPermission(sender, this);
	}

	@Override
	public void setCustomPermissionPrefix(String prefix)
	{
		String s = prefix.isEmpty() ? name : (prefix + "." + name);
		customPermission = "command." + s;
		usage = "commands." + s + ".usage";
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		if (args.length == 0)
		{
			return Collections.emptyList();
		}
		else if (isUsernameIndex(args, args.length - 1))
		{
			return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		}

		return super.getTabCompletions(server, sender, args, pos);
	}

	@Override
	public boolean isUsernameIndex(String[] args, int i)
	{
		return false;
	}

	// Static //

	public static IForgePlayer getForgePlayer(Object o) throws CommandException
	{
		IForgePlayer p = FTBLibAPI.API.getUniverse().getPlayer(o);

		if (p == null || p.isFake())
		{
			throw new PlayerNotFoundException("commands.generic.player.notFound", String.valueOf(o));
		}

		return p;
	}

	public static IForgeTeam getTeam(String s) throws CommandException
	{
		IForgeTeam team = FTBLibAPI.API.getUniverse().getTeam(s);

		if (team != null)
		{
			return team;
		}

		throw FTBLibLang.TEAM_NOT_FOUND.commandError();
	}
}