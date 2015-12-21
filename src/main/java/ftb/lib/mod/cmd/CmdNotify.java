package ftb.lib.mod.cmd;

import ftb.lib.FTBLib;
import ftb.lib.cmd.*;
import ftb.lib.mod.config.FTBLibConfigCmd;
import ftb.lib.notification.*;
import latmod.lib.LMStringUtils;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;

public class CmdNotify extends CommandLM
{
	public CmdNotify()
	{ super(FTBLibConfigCmd.Name.notify.get(), CommandLevel.OP); }
	
	public String getCommandUsage(ICommandSender ics)
	{
		StringBuilder sb = new StringBuilder();
		sb.append('/');
		sb.append(commandName);
		sb.append(" <player> <json>");
		
		if(extendedUsageInfo)
		{
			sb.append('\n');
			sb.append("Example:");
			
			Notification n = new Notification("example_id", new ChatComponentText("Example title"), 6500);
			n.setColor(0xFFFF0000);
			n.setItem(new ItemStack(Items.apple, 10));
			n.setMouseAction(new MouseAction(ClickAction.CMD, "/ftb_reload"));
			n.setDesc(new ChatComponentText("Example description"));
			sb.append(n.toJson());
			
			sb.append('\n');
			sb.append("Only \"id\" and \"title\" are required, the rest is optional");
		}
		
		return sb.toString();
	}
	
	public IChatComponent onCommand(ICommandSender ics, String[] args)
	{
		checkArgs(args, 2);
		EntityPlayerMP[] players = PlayerSelector.matchPlayers(ics, args[0]);
		
		String s = LMStringUtils.unsplitSpaceUntilEnd(1, args);
		
		try
		{
			Notification n = Notification.fromJson(s);
			
			if(n != null)
			{
				for(int i = 0; i < players.length; i++)
					if(players[i] != null) FTBLib.notifyPlayer(players[i], n);
				return null;
			}
		}
		catch(Exception e)
		{ e.printStackTrace(); }
		
		return error(new ChatComponentText("Invalid notification: " + s));
	}
}