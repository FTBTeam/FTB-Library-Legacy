package ftb.lib.cmd;

import ftb.lib.mod.FTBLibMod;
import latmod.lib.*;
import net.minecraft.command.*;
import net.minecraft.util.*;

import java.util.*;

public class CommandSubLM extends CommandLM
{
	public final HashMap<String, CommandLM> subCommands;
	
	public CommandSubLM(String s, CommandLevel l)
	{
		super(s, l);
		subCommands = new HashMap<>();
	}
	
	public void add(CommandLM c)
	{ subCommands.put(c.commandName, c); }
	
	public String getCommandUsage(ICommandSender ics)
	{
		StringBuilder sb = new StringBuilder();
		sb.append('/');
		sb.append(commandName);
		
		if(extendedUsageInfo)
		{
			ArrayList<String> l = new ArrayList<>();
			addCommandUsage(ics, l, 0);
			sb.append('\n');
			sb.append('\n');
			for(String s : l)
			{
				sb.append(s);
				sb.append('\n');
			}
			return sb.toString().trim();
		}
		
		sb.append(" [ ");
		sb.append(LMStringUtils.strip(LMMapUtils.toKeyStringArray(subCommands)));
		sb.append(" ]");
		
		return sb.toString();
	}
	
	private void addCommandUsage(ICommandSender ics, ArrayList<String> l, int level)
	{
		for(CommandLM c : subCommands.values())
		{
			if(c instanceof CommandSubLM)
			{
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < level; i++)
				{
					sb.append(' ');
					sb.append(' ');
				}
				sb.append('/' + c.commandName);
				l.add(sb.toString());
				
				((CommandSubLM) c).addCommandUsage(ics, l, level + 1);
			}
			else
			{
				if(level > 0)
				{
					StringBuilder sb = new StringBuilder();
					for(int i = 0; i < level; i++)
					{
						sb.append(' ');
						sb.append(' ');
					}
					sb.append(c.getCommandUsage(ics));
					l.add(sb.toString());
				}
				else l.add(c.getCommandUsage(ics));
			}
		}
	}
	
	public String[] getTabStrings(ICommandSender ics, String args[], int i) throws CommandException
	{
		if(i == 0) return LMMapUtils.toKeyStringArray(subCommands);
		
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
			if(cmd != null) return cmd.getUsername(LMStringUtils.shiftArray(args), i - 1);
		}
		
		return null;
	}
	
	public IChatComponent onCommand(ICommandSender ics, String[] args) throws CommandException
	{
		if(args == null || args.length == 0)
			return new ChatComponentText(LMStringUtils.strip(getTabStrings(ics, args, 0)));
		CommandLM cmd = subCommands.get(args[0]);
		if(cmd != null) return cmd.onCommand(ics, LMStringUtils.shiftArray(args));
		return new ChatComponentTranslation(FTBLibMod.mod.assets + "invalid_subcmd", args[0]);
	}
}