package ftb.lib.api.cmd;

import ftb.lib.FTBLib;
import ftb.lib.api.players.LMWorldMP;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.*;

import java.util.*;

public abstract class CommandLM extends CommandBase // CommandSubLM
{
	public final String commandName;
	public final CommandLevel level;
	public static boolean extendedUsageInfo = false;
	
	public CommandLM(String s, CommandLevel l)
	{
		if(s == null || s.isEmpty() || s.indexOf(' ') != -1)
		{
			throw new NullPointerException("Command ID can't be null!");
		}
		
		commandName = s;
		level = (l == null) ? CommandLevel.NONE : l;
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
	
	public final void processCommand(ICommandSender ics, String[] args) throws CommandException
	{
		if(level.isEnabled()) throw new FeatureDisabledException();
		if(args == null) args = new String[0];
		IChatComponent s = onCommand(ics, args);
		if(s != null) FTBLib.printChat(ics, s);
		onPostCommand(ics, args);
	}
	
	public abstract IChatComponent onCommand(ICommandSender ics, String[] args) throws CommandException;
	
	public static IChatComponent error(IChatComponent c)
	{
		c.getChatStyle().setColor(EnumChatFormatting.RED);
		return c;
	}
	
	public final void printHelpLine(ICommandSender ics, String args)
	{ FTBLib.printChat(ics, "/" + commandName + (args != null && args.length() > 0 ? (" " + args) : "")); }
	
	public void onPostCommand(ICommandSender ics, String[] args) throws CommandException
	{
	}
	
	public final List<String> addTabCompletionOptions(ICommandSender ics, String[] args, BlockPos pos)
	{
		if(level.isEnabled()) return null;
		try
		{
			String[] s = getTabStrings(ics, args, args.length - 1);
			if(s != null && s.length > 0)
			{
				if(sortStrings(ics, args, args.length - 1)) Arrays.sort(s);
				return getListOfStringsMatchingLastWord(args, s);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}
	
	public Boolean getUsername(String[] args, int i)
	{ return null; }
	
	public final boolean isUsernameIndex(String[] args, int i)
	{ return false; }
	
	public static boolean isArg(String[] args, int i, String... s)
	{
		if(args != null && i >= 0 && i < args.length)
		{
			for(String value : s) if(args[i].equals(value)) return true;
		}
		
		return false;
	}
	
	public String[] getTabStrings(ICommandSender ics, String args[], int i) throws CommandException
	{
		Boolean b = getUsername(args, i);
		if(b == null) return new String[0];
		return LMWorldMP.inst.getAllPlayerNames(b);
	}
	
	public boolean sortStrings(ICommandSender ics, String args[], int i)
	{ return getUsername(args, i) == null; }
	
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