package ftb.lib.api.cmd;

import ftb.lib.FTBLib;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;

import java.util.*;

public abstract class CommandLM extends CommandBase // CommandSubLM
{
	public final String commandName;
	public final CommandLevel level;
	
	public CommandLM(String s, CommandLevel l)
	{
		if(s == null || s.isEmpty() || s.indexOf(' ') != -1)
		{
			throw new NullPointerException("Command ID can't be null!");
		}
		
		if(l == null || l == CommandLevel.NONE) throw new NullPointerException();
		
		commandName = s;
		level = l;
	}
	
	public int getRequiredPermissionLevel()
	{ return level.requiredPermsLevel(); }
	
	public boolean canCommandSenderUseCommand(ICommandSender ics)
	{
		return FTBLib.getEffectiveSide().isClient() || level != CommandLevel.NONE && (level == CommandLevel.ALL || !FTBLib.isDedicatedServer() || super.canCommandSenderUseCommand(ics));
	}
	
	public final String getCommandName()
	{ return commandName; }
	
	public String getCommandUsage(ICommandSender ics)
	{ return '/' + commandName; }
	
	public void onPostCommand(ICommandSender ics, String[] args) throws CommandException
	{
	}
	
	public List<String> addTabCompletionOptions(ICommandSender ics, String[] args, BlockPos pos)
	{
		if(args.length == 0) return null;
		else if(isUsernameIndex(args, args.length - 1))
		{
			return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
		}
		
		return null;
	}
	
	public final String[] getTabStrings(ICommandSender ics, String[] args, int i) throws CommandException
	{
		return null;
	}
	
	public boolean isUsernameIndex(String[] args, int i)
	{ return false; }
	
	public final boolean offlineUsernames(String[] args, int i)
	{ return true; }
	
	public static boolean isArg(String[] args, int i, String... s)
	{
		if(args != null && i >= 0 && i < args.length)
		{
			for(String value : s) if(args[i].equals(value)) return true;
		}
		
		return false;
	}
	
	public static void checkArgs(String[] args, int i) throws CommandException
	{ if(args == null || args.length < i) throw new MissingArgsException(); }
	
	public static void checkArgsStrong(String[] args, int i) throws CommandException
	{ if(args == null || args.length != i) throw new MissingArgsException(); }
	
	//TODO: Fix me / make work with PlayerMatcher
	public static List<EntityPlayerMP> findPlayers(ICommandSender ics, String arg) throws CommandException
	{
		EntityPlayerMP player = null;
		
		switch(arg)
		{
			case "@a":
				return FTBLib.getAllOnlinePlayers(null);
			case "@r":
			{
				List<EntityPlayerMP> l = FTBLib.getAllOnlinePlayers(null);
				if(!l.isEmpty()) player = l.get(ics.getEntityWorld().rand.nextInt(l.size()));
				break;
			}
			case "@p":
			{
				if(ics instanceof EntityPlayerMP) return Collections.singletonList((EntityPlayerMP) ics);
				
				List<EntityPlayerMP> l = FTBLib.getAllOnlinePlayers(null);
				if(l.size() < 2) return l;
				
				EntityPlayerMP closest = null;
				double distSq = Double.POSITIVE_INFINITY;
				BlockPos c = ics.getPosition();
				
				for(EntityPlayerMP ep : l)
				{
					if(closest == null) closest = ep;
					else
					{
						double d = ep.getDistanceSq(c.getX() + 0.5D, c.getY() + 0.5D, c.getZ() + 0.5D);
						
						if(d < distSq)
						{
							distSq = d;
							closest = ep;
						}
					}
				}
				
				return Collections.singletonList(closest);
			}
			default:
				player = (EntityPlayerMP) ics.getEntityWorld().getPlayerEntityByName(arg);
				break;
		}
		
		if(player == null) return new ArrayList<>();
		return Collections.singletonList(player);
	}
	
	private static class PlayerDistanceComparator implements Comparator<EntityPlayerMP>
	{
		private final Vec3 start;
		
		private PlayerDistanceComparator(ICommandSender ics)
		{ start = ics.getPositionVector(); }
		
		public int compare(EntityPlayerMP o1, EntityPlayerMP o2)
		{ return Double.compare(start.squareDistanceTo(o2.getPositionVector()), start.squareDistanceTo(o1.getPositionVector())); }
	}
}