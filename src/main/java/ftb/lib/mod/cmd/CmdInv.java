package ftb.lib.mod.cmd;

import ftb.lib.api.cmd.*;
import net.minecraft.command.*;
import net.minecraft.util.IChatComponent;

public class CmdInv extends CommandSubLM
{
	public CmdInv()
	{
		super("ftb_inv", CommandLevel.OP);
	}
	
	public static class CmdSave extends CommandLM
	{
		public CmdSave(String s)
		{ super(s, CommandLevel.OP); }
		
		public IChatComponent onCommand(ICommandSender ics, String[] args) throws CommandException
		{
			return null;
		}
	}
	
	public static class CmdLoad extends CommandLM
	{
		public CmdLoad(String s)
		{ super(s, CommandLevel.OP); }
		
		public IChatComponent onCommand(ICommandSender ics, String[] args) throws CommandException
		{
			return null;
		}
	}
	
	public static class CmdList extends CommandLM
	{
		public CmdList(String s)
		{ super(s, CommandLevel.OP); }
		
		public IChatComponent onCommand(ICommandSender ics, String[] args) throws CommandException
		{
			return null;
		}
	}
}