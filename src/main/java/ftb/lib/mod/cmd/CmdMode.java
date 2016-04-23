package ftb.lib.mod.cmd;

import ftb.lib.FTBLib;
import ftb.lib.FTBWorld;
import ftb.lib.api.GameModes;
import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;
import ftb.lib.api.cmd.CommandSubLM;
import ftb.lib.mod.FTBLibLang;
import latmod.lib.LMStringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

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
		
		@Override
		public String getCommandUsage(ICommandSender ics)
		{ return '/' + commandName + " <modeID>"; }
		
		@Override
		public List<String> addTabCompletionOptions(ICommandSender ics, String[] args)
		{
			if(args.length == 1)
			{
				return getListOfStringsFromIterableMatchingLastWord(args, GameModes.getGameModes().modes.keySet());
			}
			
			return super.addTabCompletionOptions(ics, args);
		}
		
		@Override
		public void processCommand(ICommandSender ics, String[] args) throws CommandException
		{
			if(args.length == 0)
			{
				ics.addChatMessage(new ChatComponentText(getCommandUsage(ics)));
				return;
			}
			
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
				c = FTBLibLang.mode_loaded.chatComponent(args[0]);
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
		
		@Override
		public void processCommand(ICommandSender ics, String[] args) throws CommandException
		{
			IChatComponent c = FTBLibLang.mode_current.chatComponent(FTBWorld.server.getMode().getID());
			c.getChatStyle().setColor(EnumChatFormatting.AQUA);
			ics.addChatMessage(c);
		}
	}
	
	public static class CmdList extends CommandLM
	{
		public CmdList(String s)
		{ super(s, CommandLevel.OP); }
		
		@Override
		public void processCommand(ICommandSender ics, String[] args) throws CommandException
		{
			IChatComponent c = FTBLibLang.mode_list.chatComponent(LMStringUtils.strip(GameModes.getGameModes().modes.keySet()));
			c.getChatStyle().setColor(EnumChatFormatting.AQUA);
			ics.addChatMessage(c);
		}
	}
}