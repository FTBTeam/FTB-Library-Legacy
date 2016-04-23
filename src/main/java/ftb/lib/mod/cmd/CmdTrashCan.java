package ftb.lib.mod.cmd;

import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.item.BasicInventory;
import ftb.lib.mod.config.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;

public class CmdTrashCan extends CommandLM
{
	public CmdTrashCan()
	{ super(FTBLibConfigCmdNames.trash_can.getAsString(), FTBLibConfigCmd.level_trash_can.get()); }
	
	@Override
	public void processCommand(ICommandSender ics, String[] args) throws CommandException
	{
		EntityPlayerMP ep = getCommandSenderAsPlayer(ics);
		ep.displayGUIChest(new BasicInventory(18)
		{
			@Override
			public String getInventoryName()
			{ return "Trash Can"; }
			
			@Override
			public boolean hasCustomInventoryName()
			{ return true; }
		});
	}
}