package ftb.lib.api.cmd;

import latmod.lib.LMListUtils;
import latmod.lib.LMStringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandSubLM extends CommandLM implements ICustomCommandInfo
{
	public final Map<String, ICommand> subCommands;
	
	public CommandSubLM(String s, CommandLevel l)
	{
		super(s, l);
		subCommands = new HashMap<>();
	}
	
	public void add(ICommand c)
	{ subCommands.put(c.getCommandName(), c); }
	
	@Override
	public String getCommandUsage(ICommandSender ics)
	{ return "/" + commandName + " [ " + LMStringUtils.strip(LMListUtils.toStringArray(subCommands.keySet())) + " ]"; }
	
	@Override
	public List<String> addTabCompletionOptions(ICommandSender ics, String[] args)
	{
		if(args.length == 1) return getListOfStringsFromIterableMatchingLastWord(args, subCommands.keySet());
		
		ICommand cmd = subCommands.get(args[0]);
		
		if(cmd != null)
		{
			return cmd.addTabCompletionOptions(ics, LMStringUtils.shiftArray(args));
		}
		
		return super.addTabCompletionOptions(ics, args);
	}
	
	@Override
	public boolean isUsernameIndex(String[] args, int i)
	{
		if(i > 0 && args.length > 1)
		{
			ICommand cmd = subCommands.get(args[0]);
			if(cmd != null) return cmd.isUsernameIndex(LMStringUtils.shiftArray(args), i - 1);
		}
		
		return false;
	}
	
	@Override
	public void processCommand(ICommandSender ics, String[] args) throws CommandException
	{
		if(args == null || args.length == 0)
		{
			ics.addChatMessage(new ChatComponentText(LMStringUtils.strip(subCommands.keySet())));
		}
		else
		{
			ICommand cmd = subCommands.get(args[0]);
			if(cmd != null) cmd.processCommand(ics, LMStringUtils.shiftArray(args));
			else throw new InvalidSubCommandException(args[0]);
		}
	}
	
	@Override
	public void addInfo(List<IChatComponent> list, ICommandSender sender)
	{
		list.add(new ChatComponentText('/' + commandName));
		list.add(null);
		addCommandUsage(sender, list, 0);
	}
	
	private static IChatComponent tree(IChatComponent sibling, int level)
	{
		if(level == 0) return sibling;
		char[] chars = new char[level * 2];
		Arrays.fill(chars, ' ');
		return new ChatComponentText(new String(chars)).appendSibling(sibling);
	}
	
	private void addCommandUsage(ICommandSender ics, List<IChatComponent> list, int level)
	{
		for(ICommand c : subCommands.values())
		{
			if(c instanceof CommandSubLM)
			{
				list.add(tree(new ChatComponentText('/' + c.getCommandName()), level));
				((CommandSubLM) c).addCommandUsage(ics, list, level + 1);
			}
			else
			{
				String usage = c.getCommandUsage(ics);
				if(usage.indexOf('/') != -1 || usage.indexOf('%') != -1)
				{
					list.add(tree(new ChatComponentText(usage), level));
				}
				else
				{
					list.add(tree(new ChatComponentTranslation(usage), level));
				}
			}
		}
	}
}