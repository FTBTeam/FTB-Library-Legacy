package ftb.lib.cmd;

import ftb.lib.mod.FTBLibFinals;
import latmod.lib.*;
import net.minecraft.command.*;
import net.minecraft.util.*;

public class CommandSubLM extends CommandLM
{
	public final FastMap<String, CommandLM> subCommands;
	public static boolean extendedUsageInfo = false;
	
	public CommandSubLM(String s, CommandLevel l)
	{
		super(s, l);
		subCommands = new FastMap<String, CommandLM>();
	}
	
	public void add(CommandLM c)
	{ subCommands.put(c.commandName, c); }
	
	public String getCommandUsage(ICommandSender ics)
	{
		if(extendedUsageInfo)
		{
			FastList<String> l = new FastList<String>();
			addCommandUsage(ics, l, 0);
			StringBuilder sb = new StringBuilder('/' + commandName + "\n\n");
			for(String s : l) { sb.append(s); sb.append('\n'); }
			return sb.toString().trim();
		}
		
		return '/' + commandName + ' ' + subCommands.keys.toString();
	}
	
	private void addCommandUsage(ICommandSender ics, FastList<String> l, int level)
	{
		for(CommandLM c : subCommands)
		{
			if(c instanceof CommandSubLM)
			{
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < level; i++)
				{ sb.append(' '); sb.append(' '); }
				sb.append('/' + c.commandName);
				l.add(sb.toString());
				
				((CommandSubLM)c).addCommandUsage(ics, l, level + 1);
			}
			else
			{
				if(level > 0)
				{
					StringBuilder sb = new StringBuilder();
					for(int i = 0; i < level; i++)
					{ sb.append(' '); sb.append(' '); }
					sb.append(c.getCommandUsage(ics));
					l.add(sb.toString());
				}
				else l.add(c.getCommandUsage(ics));
			}
		}
	}
	
	public String[] getTabStrings(ICommandSender ics, String args[], int i) throws CommandException
	{
		if(i == 0) return subCommands.keys.toArray(new String[0]);
		
		CommandLM cmd = subCommands.get(args[0]);
		
		if(cmd != null)
		{
			String[] s = cmd.getTabStrings(ics, LMStringUtils.shiftArray(args), i - 1);
			if(s != null && s.length > 0) return s;
		}
		
		return super.getTabStrings(ics, args, i);
	}
	
	public Boolean getUsername(String[] args, int i)
	{
		if(i > 0 && args.length > 1)
		{
			CommandLM cmd = subCommands.get(args[0]);
			if(cmd != null)
				return cmd.getUsername(LMStringUtils.shiftArray(args), i - 1);
		}
		
		return null;
	}
	
	public IChatComponent onCommand(ICommandSender ics, String[] args) throws CommandException
	{
		if(args == null || args.length == 0)
			return new ChatComponentText(LMStringUtils.strip(getTabStrings(ics, args, 0)));
		CommandLM cmd = subCommands.get(args[0]);
		if(cmd != null) return cmd.onCommand(ics, LMStringUtils.shiftArray(args));
		return new ChatComponentTranslation(FTBLibFinals.ASSETS + "invalid_subcmd", args[0]);
	}
}