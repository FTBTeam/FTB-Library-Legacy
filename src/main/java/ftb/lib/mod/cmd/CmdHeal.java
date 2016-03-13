package ftb.lib.mod.cmd;

import ftb.lib.api.cmd.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;

public class CmdHeal extends CommandLM
{
	public CmdHeal()
	{ super("heal", CommandLevel.OP); }
	
	public void processCommand(ICommandSender ics, String[] args) throws CommandException
	{
		EntityPlayerMP ep = getCommandSenderAsPlayer(ics);
		ep.setHealth(ep.getMaxHealth());
		ep.getFoodStats().addStats(40, 40F);
		ep.extinguish();
	}
}