package ftb.lib.api.cmd;

import latmod.lib.LMStringUtils;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;

import java.util.*;

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
	{ return '/' + commandName + " [subcommand]"; }
	
	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender ics, String[] args, BlockPos pos)
	{
		if(args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args, subCommands.keySet());
		}
		
		ICommand cmd = subCommands.get(args[0]);
		
		if(cmd != null)
		{
			return cmd.getTabCompletionOptions(server, ics, LMStringUtils.shiftArray(args), pos);
		}
		
		return super.getTabCompletionOptions(server, ics, args, pos);
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
	public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
	{
		if(args.length < 1)
		{
			ics.addChatMessage(new TextComponentString(LMStringUtils.strip(subCommands.keySet())));
		}
		else
		{
			ICommand cmd = subCommands.get(args[0]);
			if(cmd == null) throw new InvalidSubCommandException(args[0]);
			else
			{
				cmd.execute(server, ics, LMStringUtils.shiftArray(args));
			}
		}
	}
	
	@Override
	public void addInfo(List<ITextComponent> list, ICommandSender sender)
	{
		list.add(new TextComponentString('/' + commandName));
		list.add(null);
		addCommandUsage(sender, list, 0);
	}
	
	private static ITextComponent tree(ITextComponent sibling, int level)
	{
		if(level == 0) return sibling;
		char[] chars = new char[level * 2];
		Arrays.fill(chars, ' ');
		return new TextComponentString(new String(chars)).appendSibling(sibling);
	}
	
	private void addCommandUsage(ICommandSender ics, List<ITextComponent> list, int level)
	{
		for(ICommand c : subCommands.values())
		{
			if(c instanceof CommandSubLM)
			{
				list.add(tree(new TextComponentString('/' + c.getCommandName()), level));
				((CommandSubLM) c).addCommandUsage(ics, list, level + 1);
			}
			else
			{
				String usage = c.getCommandUsage(ics);
				if(usage.indexOf('/') != -1 || usage.indexOf('%') != -1)
				{
					list.add(tree(new TextComponentString(usage), level));
				}
				else
				{
					list.add(tree(new TextComponentTranslation(usage), level));
				}
			}
		}
	}
}