package ftb.lib.mod.cmd;

import ftb.lib.api.cmd.*;
import latmod.lib.LMStringUtils;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

public class CmdSetItemName extends CommandLM
{
	public CmdSetItemName()
	{ super("set_item_name", CommandLevel.OP); }
	
	public String getCommandUsage(ICommandSender ics)
	{ return '/' + commandName + " <name...>"; }
	
	public void processCommand(ICommandSender ics, String[] args) throws CommandException
	{
		checkArgs(args, 1);
		EntityPlayerMP ep = getCommandSenderAsPlayer(ics);
		
		if(ep.inventory.getCurrentItem() != null)
		{
			ep.inventory.getCurrentItem().setStackDisplayName(LMStringUtils.unsplit(args, " "));
			ep.openContainer.detectAndSendChanges();
			ics.addChatMessage(new ChatComponentText("Item name set to '" + ep.inventory.getCurrentItem().getDisplayName() + "'!"));
		}
	}
}