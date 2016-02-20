package ftb.lib.mod.cmd;

import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.item.BasicInventory;
import ftb.lib.mod.config.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IChatComponent;

public class CmdTrashCan extends CommandLM
{
	public CmdTrashCan()
	{ super(FTBLibConfigCmdNames.trash_can.get(), FTBLibConfigCmd.level_trash_can.get()); }
	
	public IChatComponent onCommand(ICommandSender ics, String[] args) throws CommandException
	{
		EntityPlayerMP ep = getCommandSenderAsPlayer(ics);
		ep.displayGUIChest(new BasicInventory(18)
		{
			public String getInventoryName()
			{ return "Trash Can"; }
			
			public boolean hasCustomInventoryName()
			{ return true; }
		});
		return null;
	}
}