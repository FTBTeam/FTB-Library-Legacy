package ftb.lib.cmd;

import ftb.lib.mod.FTBLibFinals;
import latmod.lib.*;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.*;

public class CommandSubLM extends CommandLM
{
	public final FastMap<String, CommandLM> subCommands;
	
	public CommandSubLM(String s, CommandLevel l)
	{
		super(s, l);
		subCommands = new FastMap<String, CommandLM>();
	}
	
	public void add(CommandLM c)
	{ subCommands.put(c.commandName, c); }
	
	public String[] getTabStrings(ICommandSender ics, String args[], int i)
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
	
	public IChatComponent onCommand(ICommandSender ics, String[] args)
	{
		if(args == null || args.length == 0)
			return new ChatComponentText(LMStringUtils.strip(getTabStrings(ics, args, 0)));
		CommandLM cmd = subCommands.get(args[0]);
		if(cmd != null) return cmd.onCommand(ics, LMStringUtils.shiftArray(args));
		return new ChatComponentTranslation(FTBLibFinals.ASSETS + "invalid_subcmd", args[0]);
	}
}