package ftb.lib.mod.cmd;

import com.google.gson.JsonPrimitive;
import ftb.lib.FTBLib;
import ftb.lib.api.cmd.*;
import ftb.lib.api.notification.*;
import latmod.lib.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;

import java.util.List;

public class CmdNotify extends CommandLM implements ICustomCommandInfo
{
	public CmdNotify()
	{ super("ftb_notify", CommandLevel.OP); }
	
	public String getCommandUsage(ICommandSender ics)
	{ return "/" + commandName + " <player|@a> <json...>"; }
	
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
			Notification n = Notification.deserialize(LMJsonUtils.fromJson(s));
			
			if(n != null)
			{
				for(EntityPlayerMP ep : findPlayers(ics, args[0]))
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
	
	public void addInfo(List<IChatComponent> list, ICommandSender sender)
	{
		list.add(new ChatComponentText("/" + commandName));
		list.add(null);
		
		list.add(new ChatComponentText("Example:"));
		list.add(null);
		
		Notification n = new Notification("example_id", new ChatComponentText("Example title"), 6500);
		n.setColor(0xFFFF0000);
		n.setItem(new ItemStack(Items.apple, 10));
		MouseAction ma = new MouseAction();
		ma.click = new ClickAction(ClickActionType.CMD, new JsonPrimitive("reload"));
		n.setMouseAction(ma);
		n.setDesc(new ChatComponentText("Example description"));
		
		for(String s : LMJsonUtils.toJson(LMJsonUtils.getGson(true), n.getSerializableElement()).split("\n"))
		{
			list.add(new ChatComponentText(s));
		}
		
		list.add(null);
		list.add(new ChatComponentText("Only \"id\" and \"title\" are required, the rest is optional"));
		list.add(new ChatComponentText("\"mouse\":{} will make it permanent"));
	}
}