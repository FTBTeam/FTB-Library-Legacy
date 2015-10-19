package ftb.lib;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import ftb.lib.api.GameModes;
import ftb.lib.mod.FTBLib;
import net.minecraft.command.*;
import net.minecraft.util.*;

public class CommandFTBLMode extends CommandBase
{
	public String getCommandName()
	{ return "ftb_mode"; }
	
	public String getCommandUsage(ICommandSender ics)
	{ return "/ftb_mode [set | get | list] [modeID]"; }
	
	public int getRequiredPermissionLevel()
	{ return 4; }
	
	@SuppressWarnings("all")
	public List addTabCompletionOptions(ICommandSender ics, String[] args)
	{
		if(args.length == 1)
			return getListOfStringsMatchingLastWord(args, "get", "set", "list");
		else if(args.length == 2 && args[0].equals("set"))
			return getListOfStringsFromIterableMatchingLastWord(args, FTBLib.getAllModes().allModes);
		return null;
	}
	
	public void processCommand(ICommandSender ics, String[] args)
	{
		IChatComponent c = execCmd(ics, args);
		if(c != null) ics.addChatMessage(c);
		else throw new IllegalArgumentException((args.length > 0) ? args[0] : "No arguments!");
	}
	
	public IChatComponent execCmd(ICommandSender ics, String[] args)
	{
		if(args == null || args.length == 0)
			return new ChatComponentText(getCommandUsage(ics));
		
		GameModes list = FTBLib.getAllModes();
		
		if(args[0].equals("set"))
		{
			if(args.length == 1)
				return new ChatComponentText(getCommandUsage(ics));
			
			IChatComponent c;
			
			int i = FTBLib.setMode(Side.SERVER, args[1], false, false);
			
			if(i == 1)
			{
				c = new ChatComponentTranslation("ftbl:gamemode_not_found");
				c.getChatStyle().setColor(EnumChatFormatting.RED);
			}
			else if(i == 2)
			{
				c = new ChatComponentTranslation("ftbl:gamemode_already_set");
				c.getChatStyle().setColor(EnumChatFormatting.RED);
			}
			else
			{
				c = new ChatComponentTranslation("ftbl:gamemode_loaded");
				c.getChatStyle().setColor(EnumChatFormatting.GREEN);
			}
			
			return c;
		}
		else if(args[0].equals("get"))
		{
			IChatComponent c = new ChatComponentTranslation("ftbl:gamemode_current", FTBLib.getMode());
			c.getChatStyle().setColor(EnumChatFormatting.BLUE);
			return c;
		}
		else if(args[0].equals("list"))
		{
			StringBuilder sb = new StringBuilder();
			
			int i = -1;
			for(String s : list.allModes)
			{
				sb.append(s);
				i++;
				if(i != list.allModes.size() - 1)
					sb.append(", ");
			}
			
			IChatComponent c = new ChatComponentTranslation("ftbl:gamemode_list", sb.toString());
			c.getChatStyle().setColor(EnumChatFormatting.BLUE);
			return c;
		}
		
		return null;
	}
}