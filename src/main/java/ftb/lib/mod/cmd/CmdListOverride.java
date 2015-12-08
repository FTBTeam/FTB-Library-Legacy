package ftb.lib.mod.cmd;

import ftb.lib.FTBLib;
import ftb.lib.cmd.*;
import latmod.lib.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IChatComponent;

public class CmdListOverride extends CommandLM
{
	public CmdListOverride()
	{ super("list", CommandLevel.ALL); }
	
	public String getCommandUsage(ICommandSender ics)
	{ return '/' + commandName + " ['uuid']"; }
	
	public IChatComponent onCommand(ICommandSender ics, String[] args) throws CommandException
	{
		FastList<EntityPlayerMP> players = FTBLib.getAllOnlinePlayers(null);
		boolean printUUID = args.length > 0 && args[0].equals("uuid");
		
		FTBLib.printChat(ics, "Players currently online: [ " + players.size() + " ]");
		for(int i = 0; i < players.size(); i++)
		{
			EntityPlayerMP ep = players.get(i);
			
			if(printUUID)
				FTBLib.printChat(ics, ep.getCommandSenderName() + " :: " + LMStringUtils.fromUUID(ep.getUniqueID()));
			else
				FTBLib.printChat(ics, ep.getCommandSenderName());
		}
		
		return null;
	}
}