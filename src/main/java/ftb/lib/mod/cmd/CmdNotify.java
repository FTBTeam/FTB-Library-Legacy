package ftb.lib.mod.cmd;

import com.google.gson.JsonPrimitive;
import ftb.lib.FTBLib;
import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;
import ftb.lib.api.cmd.ICustomCommandInfo;
import ftb.lib.api.notification.ClickAction;
import ftb.lib.api.notification.ClickActionType;
import ftb.lib.api.notification.MouseAction;
import ftb.lib.api.notification.Notification;
import latmod.lib.LMJsonUtils;
import latmod.lib.LMStringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.util.Collections;
import java.util.List;

public class CmdNotify extends CommandLM implements ICustomCommandInfo
{
	public CmdNotify()
	{ super("ftb_notify", CommandLevel.OP); }
	
	@Override
	public String getCommandUsage(ICommandSender ics)
	{ return "/" + commandName + " <player|@a> <json...>"; }
	
	@Override
	public List<String> addTabCompletionOptions(ICommandSender ics, String[] args)
	{
		if(args.length == 2) return Collections.singletonList("{\"id\":\"test\", \"title\":\"Title\", \"mouse\":{}}");
		return super.addTabCompletionOptions(ics, args);
	}
	
	@Override
	public boolean isUsernameIndex(String[] args, int i)
	{ return i == 0; }
	
	@Override
	public void processCommand(ICommandSender ics, String[] args) throws CommandException
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
				return;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		error("Invalid notification: " + s);
	}
	
	@Override
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