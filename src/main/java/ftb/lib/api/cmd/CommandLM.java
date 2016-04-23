package ftb.lib.api.cmd;

import ftb.lib.FTBLib;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CommandLM extends CommandBase // CommandFTBU CommandSubLM
{
	public final String commandName;
	public final CommandLevel level;
	
	public CommandLM(String s, CommandLevel l)
	{
		commandName = ((s == null) ? "" : s).trim();
		level = (l == null || commandName.isEmpty()) ? CommandLevel.NONE : l;
	}
	
	@Override
	public int getRequiredPermissionLevel()
	{ return level.requiredPermsLevel(); }
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender ics)
	{
		if(FTBLib.getEffectiveSide().isClient()) return true;
		return level != CommandLevel.NONE && (level == CommandLevel.ALL || !FTBLib.isDedicatedServer() || super.canCommandSenderUseCommand(ics));
	}
	
	@Override
	public final String getCommandName()
	{ return commandName; }
	
	@Override
	public String getCommandUsage(ICommandSender ics)
	{ return '/' + commandName; }
	
	public static void error(String o)
	{ throw new RawCommandError(o); }
	
	@SuppressWarnings("all")
	public List<String> addTabCompletionOptions(ICommandSender ics, String[] args)
	{
		if(!level.isEnabled()) return null;
		
		if(isUsernameIndex(args, args.length - 1))
		{
			return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
		}
		
		return null;
	}
	
	@Override
	public boolean isUsernameIndex(String[] args, int i)
	{ return false; }
	
	public static boolean isArg(String[] args, int i, String... s)
	{
		if(args != null && i >= 0 && i < args.length)
		{
			for(int j = 0; j < s.length; j++)
				if(args[i].equals(s[j])) return true;
		}
		
		return false;
	}
	
	public static void checkArgs(String[] args, int i) throws CommandException
	{ if(args == null || args.length < i) throw new MissingArgsException(); }
	
	public static void checkArgsStrong(String[] args, int i) throws CommandException
	{ if(args == null || args.length != i) throw new MissingArgsException(); }
	
	public static List<EntityPlayerMP> findPlayers(ICommandSender ics, String arg) throws CommandException
	{
		EntityPlayerMP player = null;
		
		if(arg.equals("@a")) return FTBLib.getAllOnlinePlayers(null);
		else if(arg.equals("@r"))
		{
			List<EntityPlayerMP> l = FTBLib.getAllOnlinePlayers(null);
			if(!l.isEmpty()) player = l.get(ics.getEntityWorld().rand.nextInt(l.size()));
		}
		else if(arg.equals("@p"))
		{
			if(ics instanceof EntityPlayerMP) return Collections.singletonList((EntityPlayerMP) ics);
			
			List<EntityPlayerMP> l = FTBLib.getAllOnlinePlayers(null);
			if(l.size() < 2) return l;
			
			EntityPlayerMP closest = null;
			double distSq = Double.POSITIVE_INFINITY;
			ChunkCoordinates c = ics.getPlayerCoordinates();
			
			for(EntityPlayerMP ep : l)
			{
				if(closest == null) closest = ep;
				else
				{
					double d = ep.getDistanceSq(c.posX + 0.5D, c.posY + 0.5D, c.posZ + 0.5D);
					
					if(d < distSq)
					{
						distSq = d;
						closest = ep;
					}
				}
			}
			
			return Collections.singletonList(closest);
		}
		else
		{
			player = (EntityPlayerMP) ics.getEntityWorld().getPlayerEntityByName(arg);
		}
		
		if(player == null) return new ArrayList<>();
		return Collections.singletonList(player);
	}
}