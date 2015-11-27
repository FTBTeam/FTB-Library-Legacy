package ftb.lib.mod;

import cpw.mods.fml.relauncher.Side;
import ftb.lib.*;
import ftb.lib.api.GameModes;
import ftb.lib.cmd.*;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.*;

public class CommandFTBMode extends CommandLM
{
	public CommandFTBMode()
	{ super("ftb_mode", CommandLevel.OP); }
	
	public String getCommandUsage(ICommandSender ics)
	{ return "/ftb_mode [set <modeID> | get | list]"; }
	
	public boolean canCommandSenderUseCommand(ICommandSender ics)
	{ return !FTBLib.getServer().isDedicatedServer() || super.canCommandSenderUseCommand(ics); }
	
	public String[] getTabStrings(ICommandSender ics, String[] args, int i)
	{
		if(args.length == 1) return new String[] { "get", "set", "list" };
		else if(args.length == 2 && args[0].equals("set"))
			return FTBWorld.getAllModes().allModes.toArray(new String[0]);
		return null;
	}
	
	public IChatComponent onCommand(ICommandSender ics, String[] args)
	{
		if(args == null || args.length == 0)
			return new ChatComponentText(getCommandUsage(ics));
		
		GameModes list = FTBWorld.getAllModes();
		
		if(args[0].equals("set"))
		{
			if(args.length == 1) return new ChatComponentText(getCommandUsage(ics));
			
			IChatComponent c;
			
			int i = FTBWorld.server.setMode(Side.SERVER, args[1], false);
			
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
			IChatComponent c = new ChatComponentTranslation("ftbl:gamemode.current", FTBWorld.server.getMode());
			c.getChatStyle().setColor(EnumChatFormatting.BLUE);
			return c;
		}
		else if(args[0].equals("list"))
		{
			IChatComponent c = new ChatComponentTranslation("ftbl:gamemode.list", joinNiceStringFromCollection(list.allModes));
			c.getChatStyle().setColor(EnumChatFormatting.BLUE);
			return c;
		}
		
		return null;
	}
}