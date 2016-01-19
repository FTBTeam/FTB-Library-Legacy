package ftb.lib.mod.cmd;

import com.google.gson.JsonPrimitive;
import ftb.lib.FTBLib;
import ftb.lib.api.cmd.*;
import ftb.lib.mod.config.FTBLibConfigCmd;
import ftb.lib.notification.*;
import latmod.lib.*;
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
			sb.append("\nExample:\n");
			
			Notification n = new Notification("example_id", new ChatComponentText("Example title"), 6500);
			n.setColor(0xFFFF0000);
			n.setItem(new ItemStack(Items.apple, 10));
			n.setMouseAction(new MouseAction(ClickAction.CMD, new JsonPrimitive("/ftb_reload")));
			n.setDesc(new ChatComponentText("Example description"));
			sb.append(LMJsonUtils.toJson(LMJsonUtils.getGson(true), n));
			
			sb.append('\n');
			sb.append("Only \"id\" and \"title\" are required, the rest is optional");
		}
		
		return sb.toString();
	}
	
	public String[] getTabStrings(ICommandSender ics, String args[], int i) throws CommandException
	{
		if(i == 1) return new String[] {"{\"id\":\"test\", \"title\":\"Title\", \"mouse\":{}}"};
		return super.getTabStrings(ics, args, i);
	}
	
	public Boolean getUsername(String[] args, int i)
	{ return (i == 0) ? Boolean.TRUE : null; }
	
	public IChatComponent onCommand(ICommandSender ics, String[] args) throws CommandException
	{
		checkArgs(args, 2);
		
		String s = LMStringUtils.unsplitSpaceUntilEnd(1, args);
		
		try
		{
			Notification n = Notification.fromJson(s);
			
			if(n != null)
			{
				for(EntityPlayerMP ep : PlayerSelector.matchPlayers(ics, args[0]))
					FTBLib.notifyPlayer(ep, n);
				return null;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return error(new ChatComponentText("Invalid notification: " + s));
	}
}