package ftb.lib.mod;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import ftb.lib.FTBGameModes;
import ftb.lib.api.GameModes;
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
			return getListOfStringsFromIterableMatchingLastWord(args, FTBGameModes.getAllModes().allModes);
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
		
		GameModes list = FTBGameModes.getAllModes();
		
		if(args[0].equals("set"))
		{
			if(args.length == 1)
				return new ChatComponentText(getCommandUsage(ics));
			
			IChatComponent c;
			
			int i = FTBGameModes.setMode(Side.SERVER, args[1], false);
			
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
				c = new ChatComponentTranslation("ftbl:gamemode.loaded", args[1]);
				c.getChatStyle().setColor(EnumChatFormatting.GREEN);
			}
			
			return c;
		}
		else if(args[0].equals("get"))
		{
			IChatComponent c = new ChatComponentTranslation("ftbl:gamemode.current", FTBGameModes.getMode());
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
			
			IChatComponent c = new ChatComponentTranslation("ftbl:gamemode.list", sb.toString());
			c.getChatStyle().setColor(EnumChatFormatting.BLUE);
			return c;
		}
		
		return null;
	}
}