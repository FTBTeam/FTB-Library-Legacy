package ftb.lib.mod.cmd;

import ftb.lib.FTBLib;
import ftb.lib.api.*;
import ftb.lib.api.cmd.*;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;

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
		
		public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender ics, String[] args, BlockPos pos)
		{
			if(args.length == 1)
			{
				return getListOfStringsMatchingLastWord(args, GameModes.instance().modes.keySet());
			}
			
			return super.getTabCompletionOptions(server, ics, args, pos);
		}
		
		public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
		{
			if(args.length == 0)
			{
				ics.addChatMessage(new TextComponentString(getCommandUsage(ics)));
				return;
			}
			
			ITextComponent c;
			
			int i = ForgeWorldMP.inst.setMode(args[0]);
			
			if(i == 1)
			{
				c = new TextComponentTranslation("ftbl:gamemode.not_found");
				c.getChatStyle().setColor(TextFormatting.RED);
			}
			else if(i == 2)
			{
				c = new TextComponentTranslation("ftbl:gamemode.already_set");
				c.getChatStyle().setColor(TextFormatting.RED);
			}
			else
			{
				c = new TextComponentTranslation("ftbl:gamemode.loaded", args[0]);
				c.getChatStyle().setColor(TextFormatting.GREEN);
				FTBLib.reload(ics, true, true);
			}
			
			ics.addChatMessage(c);
		}
	}
	
	public static class CmdGet extends CommandLM
	{
		public CmdGet(String s)
		{ super(s, CommandLevel.OP); }
		
		public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
		{
			ITextComponent c = new TextComponentTranslation("ftbl:gamemode.current", ForgeWorldMP.inst.getMode());
			c.getChatStyle().setColor(TextFormatting.AQUA);
			ics.addChatMessage(c);
		}
	}
	
	public static class CmdList extends CommandLM
	{
		public CmdList(String s)
		{ super(s, CommandLevel.OP); }
		
		public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
		{
			ITextComponent c = new TextComponentTranslation("ftbl:gamemode.list", joinNiceStringFromCollection(GameModes.instance().modes.keySet()));
			c.getChatStyle().setColor(TextFormatting.AQUA);
			ics.addChatMessage(c);
		}
	}
}