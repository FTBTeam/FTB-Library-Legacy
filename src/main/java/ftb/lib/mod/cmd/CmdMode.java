package ftb.lib.mod.cmd;

import ftb.lib.FTBLib;
import ftb.lib.api.GameModes;
import ftb.lib.api.cmd.*;
import ftb.lib.api.players.ForgeWorldMP;
import net.minecraft.command.*;
import net.minecraft.util.*;

import java.util.List;

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
		
		public List<String> addTabCompletionOptions(ICommandSender ics, String[] args, BlockPos pos)
		{
			if(args.length == 1)
			{
				return getListOfStringsMatchingLastWord(args, GameModes.getGameModes().modes.keySet());
			}
			return null;
		}
		
		public void processCommand(ICommandSender ics, String[] args) throws CommandException
		{
			if(args.length == 0)
			{
				ics.addChatMessage(new ChatComponentText(getCommandUsage(ics)));
				return;
			}
			
			IChatComponent c;
			
			int i = ForgeWorldMP.inst.setMode(args[0]);
			
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
			
			ics.addChatMessage(c);
		}
	}
	
	public static class CmdGet extends CommandLM
	{
		public CmdGet(String s)
		{ super(s, CommandLevel.OP); }
		
		public void processCommand(ICommandSender ics, String[] args) throws CommandException
		{
			IChatComponent c = new ChatComponentTranslation("ftbl:gamemode.current", ForgeWorldMP.inst.getMode());
			c.getChatStyle().setColor(EnumChatFormatting.AQUA);
			ics.addChatMessage(c);
		}
	}
	
	public static class CmdList extends CommandLM
	{
		public CmdList(String s)
		{ super(s, CommandLevel.OP); }
		
		public void processCommand(ICommandSender ics, String[] args) throws CommandException
		{
			IChatComponent c = new ChatComponentTranslation("ftbl:gamemode.list", joinNiceStringFromCollection(GameModes.getGameModes().modes.keySet()));
			c.getChatStyle().setColor(EnumChatFormatting.AQUA);
			ics.addChatMessage(c);
		}
	}
}