package ftb.lib.mod.cmd;

import ftb.lib.api.cmd.CommandLM;
import ftb.lib.mod.config.FTBLibConfigCmd;
import ftb.lib.mod.config.FTBLibConfigCmdNames;
import latmod.lib.LMStringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

public class CmdSetItemName extends CommandLM
{
	public CmdSetItemName()
	{ super(FTBLibConfigCmdNames.set_item_name.getAsString(), FTBLibConfigCmd.level_set_item_name.get()); }
	
	@Override
	public String getCommandUsage(ICommandSender ics)
	{ return '/' + commandName + " <name...>"; }
	
	@Override
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