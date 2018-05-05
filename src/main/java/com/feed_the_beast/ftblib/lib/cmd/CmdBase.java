package com.feed_the_beast.ftblib.lib.cmd;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.math.MathUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.server.permission.PermissionAPI;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class CmdBase extends CommandBase implements ICommandWithParent
{
	public enum Level
	{
		ALL()
				{
					@Override
					public boolean checkPermission(MinecraftServer server, ICommandSender sender, ICommand command)
					{
						return true;
					}
				},
		OP_OR_SP()
				{
					@Override
					public boolean checkPermission(MinecraftServer server, ICommandSender sender, ICommand command)
					{
						return !server.isDedicatedServer() || sender.canUseCommand(2, command.getName());
					}
				},
		OP()
				{
					@Override
					public boolean checkPermission(MinecraftServer server, ICommandSender sender, ICommand command)
					{
						return sender.canUseCommand(2, command.getName());
					}
				},
		SERVER()
				{
					@Override
					public boolean checkPermission(MinecraftServer server, ICommandSender sender, ICommand command)
					{
						return sender instanceof MinecraftServer;
					}
				};

		public abstract boolean checkPermission(MinecraftServer server, ICommandSender sender, ICommand command);
	}

	protected static final List<String> LIST_TRUE_FALSE = Collections.unmodifiableList(Arrays.asList("true", "false"));

	public void checkArgs(ICommandSender sender, String[] args, int i) throws CommandException
	{
		if (args.length < i)
		{
			throw new WrongUsageException(getUsage(sender));
		}
	}

	private final String name;
	public final Level level;
	private ICommand parent;

	public CmdBase(String n, Level l)
	{
		name = n;
		level = l;
	}

	@Override
	public final String getName()
	{
		return name;
	}

	@Override
	public final int getRequiredPermissionLevel()
	{
		return 2;
	}

	@Override
	public String getUsage(ICommandSender ics)
	{
		return "commands." + ICommandWithParent.getFullPath(this) + ".usage";
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return level.checkPermission(server, sender, this);
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

	@Override
	public void setParent(@Nullable ICommand p)
	{
		parent = p;
	}

	@Override
	@Nullable
	public ICommand getParent()
	{
		return parent;
	}

	// Static //

	public static ForgePlayer getForgePlayer(ICommandSender sender) throws CommandException
	{
		ForgePlayer p = Universe.get().getPlayer(sender);

		if (p.isFake())
		{
			throw new CommandException("commands.generic.player.notFound", sender.getName());
		}

		return p;
	}

	public static ForgePlayer getForgePlayer(ICommandSender sender, String name) throws CommandException
	{
		ForgePlayer p;

		switch (name)
		{
			case "@r":
			{
				ForgePlayer[] players = Universe.get().getOnlinePlayers().toArray(new ForgePlayer[0]);
				p = players.length == 0 ? null : players[MathUtils.RAND.nextInt(players.length)];
				break;
			}
			case "@ra":
			{
				ForgePlayer[] players = Universe.get().getPlayers().toArray(new ForgePlayer[0]);
				p = players.length == 0 ? null : players[MathUtils.RAND.nextInt(players.length)];
				break;
			}
			case "@p":
			{
				p = null;
				double dist = Double.POSITIVE_INFINITY;

				for (ForgePlayer p1 : Universe.get().getOnlinePlayers())
				{
					if (p == null)
					{
						p = p1;
					}
					else
					{
						Vec3d pos = sender.getPositionVector();
						double d = p1.getPlayer().getDistanceSq(pos.x, pos.y, pos.z);

						if (d < dist)
						{
							dist = d;
							p = p1;
						}
					}
				}
				break;
			}
			default:
				p = Universe.get().getPlayer(name);
		}

		if (p == null || p.isFake())
		{
			throw new CommandException("commands.generic.player.notFound", name);
		}

		return p;
	}

	public static ForgeTeam getTeam(String s) throws CommandException
	{
		ForgeTeam team = Universe.get().getTeam(s);

		if (team.isValid())
		{
			return team;
		}

		throw new CommandException("ftblib.lang.team.error.not_found", s);
	}

	public static EntityPlayerMP getSelfOrOther(ICommandSender sender, String[] args, int index) throws CommandException
	{
		return getSelfOrOther(sender, args, index, "");
	}

	public static EntityPlayerMP getSelfOrOther(ICommandSender sender, String[] args, int index, String specialPerm) throws CommandException
	{
		if (args.length <= index)
		{
			return getCommandSenderAsPlayer(sender);
		}
		else if (!specialPerm.isEmpty() && sender instanceof EntityPlayerMP && !PermissionAPI.hasPermission((EntityPlayerMP) sender, specialPerm))
		{
			throw new CommandException("commands.generic.permission");
		}

		return getForgePlayer(sender, args[index]).getCommandPlayer();
	}
}