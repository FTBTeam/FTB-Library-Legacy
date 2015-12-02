package ftb.lib.cmd;

import java.util.*;

import ftb.lib.FTBLib;
import latmod.lib.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.*;
import net.minecraftforge.common.UsernameCache;

public abstract class CommandLM extends CommandBase // CommandFTBU
{
	public final String commandName;
	public final CommandLevel level;
	
	public CommandLM(String s, CommandLevel l)
	{
		commandName = ((s == null) ? "" : s).trim();
		level = (l == null || commandName.isEmpty()) ? CommandLevel.NONE : l;
	}
	
	public int getRequiredPermissionLevel()
	{ return level.requiredPermsLevel(); }
	
	public boolean canCommandSenderUseCommand(ICommandSender ics)
	{ return level != CommandLevel.NONE && (level == CommandLevel.ALL || super.canCommandSenderUseCommand(ics)); }
	
	public final String getCommandName()
	{ return commandName; }
	
	public String getCommandUsage(ICommandSender ics)
	{ return '/' + commandName; }
	
	public final void processCommand(ICommandSender ics, String[] args)
	{
		if(!level.isEnabled()) throw new FeatureDisabledException();
		if(args == null) args = new String[0];
		IChatComponent s = onCommand(ics, args);
		if(s != null) FTBLib.printChat(ics, s);
		onPostCommand(ics, args);
	}
	
	public abstract IChatComponent onCommand(ICommandSender ics, String[] args) throws CommandException;
	
	public static IChatComponent error(IChatComponent c)
	{ c.getChatStyle().setColor(EnumChatFormatting.RED); return c; }
	
	public final void printHelpLine(ICommandSender ics, String args)
	{ FTBLib.printChat(ics, "/" + commandName + (args != null && args.length() > 0 ? (" " + args) : "")); }
	
	public void onPostCommand(ICommandSender ics, String[] args) {}
	
	@SuppressWarnings("all")
	public final List addTabCompletionOptions(ICommandSender ics, String[] args)
	{
		if(!level.isEnabled()) return null;
		String[] s = getTabStrings(ics, args, args.length - 1);
		if(s != null && s.length > 0)
		{
			if(sortStrings(ics, args, args.length - 1)) Arrays.sort(s);
			return getListOfStringsMatchingLastWord(args, s);
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
			for(int j = 0; j < s.length; j++)
				if(args[i].equals(s[j])) return true;
		}
		
		return false;
	}
	
	public String[] getTabStrings(ICommandSender ics, String args[], int i) throws CommandException
	{
		Boolean b = getUsername(args, i);
		if(b == null) return new String[0];
		if(!b) return UsernameCache.getMap().values().toArray(new String[0]);
		FastList<String> l = new FastList<String>();
		FastList<EntityPlayerMP> players = FTBLib.getAllOnlinePlayers(null);
		for(int j = 0; j < players.size(); j++)
			l.add(players.get(j).getCommandSenderName());
		return l.toStringArray();
	}
	
	public boolean sortStrings(ICommandSender ics, String args[], int i)
	{ return getUsername(args, i) == null; }
	
	public static void checkArgs(String[] args, int i)
	{ if(args == null || args.length < i) throw new MissingArgsException(); }
	
	public static void checkArgsStrong(String[] args, int i)
	{ if(args == null || args.length != i) throw new MissingArgsException(); }
	
	public static int parseRelInt(ICommandSender ics, int n, String s)
	{ return MathHelperLM.floor(func_110666_a(ics, n, s)); }
}