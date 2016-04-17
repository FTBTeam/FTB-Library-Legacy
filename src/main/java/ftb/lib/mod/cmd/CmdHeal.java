package ftb.lib.mod.cmd;

import ftb.lib.api.cmd.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CmdHeal extends CommandLM
{
	public CmdHeal()
	{ super("heal", CommandLevel.OP); }
	
	public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
	{
		EntityPlayerMP ep = getCommandSenderAsPlayer(ics);
		ep.setHealth(ep.getMaxHealth());
		ep.getFoodStats().addStats(40, 40F);
		ep.extinguish();
	}
}