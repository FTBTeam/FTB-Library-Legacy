package ftb.lib.mod.cmd;

import ftb.lib.*;
import ftb.lib.api.GameModes;
import ftb.lib.api.cmd.*;
import ftb.lib.mod.FTBLibLang;
import latmod.lib.*;
import net.minecraft.command.*;
import net.minecraft.util.*;

public class CmdMode extends CommandSubLM
{
	public CmdMode()
	{
		super("ftb_mode", CommandLevel.OP);
		add(new CmdSet("set"));
		add(new CmdGet("get"));
		add(new CmdList("list"));
	}
	
	public static class CmdSet extends CommandLM
	{
		public CmdSet(String s)
		{ super(s, CommandLevel.OP); }
		
		public String getCommandUsage(ICommandSender ics)
		{ return '/' + commandName + " <modeID>"; }
		
		public String[] getTabStrings(ICommandSender ics, String[] args, int i) throws CommandException
		{
			if(args.length == 1) return LMListUtils.toStringArray(GameModes.getGameModes().modes.keySet());
			return super.getTabStrings(ics, args, i);
		}
		
		public IChatComponent onCommand(ICommandSender ics, String[] args) throws CommandException
		{
			if(args.length == 0) return new ChatComponentText(getCommandUsage(ics));
			
			IChatComponent c;
			
			int i = FTBWorld.server.setMode(args[0]);
			
			if(i == 1)
			{
				c = FTBLibLang.mode_not_found.chatComponent();
				c.getChatStyle().setColor(EnumChatFormatting.RED);
			}
			else if(i == 2)
			{
				c = FTBLibLang.mode_already_set.chatComponent();
				c.getChatStyle().setColor(EnumChatFormatting.RED);
			}
			else
			{
				c = FTBLibLang.mode_loaded.chatComponent();
				c.getChatStyle().setColor(EnumChatFormatting.GREEN);
				FTBLib.reload(ics, true, true);
			}
			
			return c;
		}
	}
	
	public static class CmdGet extends CommandLM
	{
		public CmdGet(String s)
		{ super(s, CommandLevel.OP); }
		
		public IChatComponent onCommand(ICommandSender ics, String[] args) throws CommandException
		{
			IChatComponent c = FTBLibLang.mode_current.chatComponent(FTBWorld.server.getMode().getID());
			c.getChatStyle().setColor(EnumChatFormatting.AQUA);
			return c;
		}
	}
	
	public static class CmdList extends CommandLM
	{
		public CmdList(String s)
		{ super(s, CommandLevel.OP); }
		
		public IChatComponent onCommand(ICommandSender ics, String[] args) throws CommandException
		{
			IChatComponent c = FTBLibLang.mode_list.chatComponent(LMStringUtils.strip(GameModes.getGameModes().modes.keySet()));
			c.getChatStyle().setColor(EnumChatFormatting.AQUA);
			return c;
		}
	}
}