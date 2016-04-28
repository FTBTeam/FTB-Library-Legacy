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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

public class CmdNotify extends CommandLM implements ICustomCommandInfo
{
	public CmdNotify()
	{ super("ftb_notify", CommandLevel.OP); }
	
	@Override
	public String getCommandUsage(ICommandSender ics)
	{ return "/" + commandName + " <player|@a> <json...>"; }
	
	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender ics, String[] args, BlockPos pos)
	{
		if(args.length == 2)
		{
			return getListOfStringsMatchingLastWord(args, "{\"id\":\"test\", \"title\":\"Title\", \"mouse\":{}}");
		}
		
		return super.getTabCompletionOptions(server, ics, args, pos);
	}
	
	@Override
	public boolean isUsernameIndex(String[] args, int i)
	{ return i == 0; }
	
	@Override
	public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
	{
		checkArgs(args, 2);
		
		String s = LMStringUtils.unsplitSpaceUntilEnd(1, args);
		
		Notification n = Notification.deserialize(LMJsonUtils.fromJson(s));
		
		for(EntityPlayerMP ep : findPlayers(ics, args[0]))
		{
			FTBLib.notifyPlayer(ep, n);
		}
	}
	
	@Override
	public void addInfo(List<ITextComponent> list, ICommandSender sender)
	{
		list.add(new TextComponentString("/" + commandName));
		list.add(null);
		
		list.add(new TextComponentString("Example:"));
		list.add(null);
		
		Notification n = new Notification("example_id", new TextComponentString("Example title"), 6500);
		n.setColor(0xFFFF0000);
		n.setItem(new ItemStack(Items.apple, 10));
		MouseAction ma = new MouseAction();
		ma.click = new ClickAction(ClickActionType.CMD, new JsonPrimitive("reload"));
		n.setMouseAction(ma);
		n.setDesc(new TextComponentString("Example description"));
		
		for(String s : LMJsonUtils.toJson(LMJsonUtils.getGson(true), n.getSerializableElement()).split("\n"))
		{
			list.add(new TextComponentString(s));
		}
		
		list.add(null);
		list.add(new TextComponentString("Only \"id\" and \"title\" are required, the rest is optional"));
		list.add(new TextComponentString("\"mouse\":{} will make it permanent"));
	}
}