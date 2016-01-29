package ftb.lib.mod.cmd;

import ftb.lib.*;
import ftb.lib.api.GameModes;
import ftb.lib.api.cmd.*;
import latmod.lib.LMMapUtils;
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
			if(args.length == 1) return LMMapUtils.toKeyStringArray(GameModes.getGameModes().modes);
			return super.getTabStrings(ics, args, i);
		}
		
		public IChatComponent onCommand(ICommandSender ics, String[] args) throws CommandException
		{
			if(args.length == 0) return new ChatComponentText(getCommandUsage(ics));
			
			IChatComponent c;
			
			int i = FTBWorld.server.setMode(args[0]);
			
			if(i == 1)
			{
				c = new ChatComponentTranslation("ftbl:gamemode.not_found");
				c.getChatStyle().setColor(EnumChatFormatting.RED);
			}
			else if(i == 2)
			{
				c = new ChatComponentTranslation("ftbl:gamemode.already_set");
				c.getChatStyle().setColor(EnumChatFormatting.RED);
			}
			else
			{
				c = new ChatComponentTranslation("ftbl:gamemode.loaded", args[0]);
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
			IChatComponent c = new ChatComponentTranslation("ftbl:gamemode.current", FTBWorld.server.getMode());
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
			IChatComponent c = new ChatComponentTranslation("ftbl:gamemode.list", joinNiceStringFromCollection(GameModes.getGameModes().modes.keySet()));
			c.getChatStyle().setColor(EnumChatFormatting.AQUA);
			return c;
		}
	}
}