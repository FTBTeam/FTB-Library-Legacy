package ftb.lib.mod.cmd;

import ftb.lib.FTBLib;
import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;
import latmod.lib.LMUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

public class CmdListOverride extends CommandLM
{
	public CmdListOverride()
	{ super("list", CommandLevel.ALL); }
	
	@Override
	public String getCommandUsage(ICommandSender ics)
	{ return '/' + commandName + " ['uuid']"; }
	
	@Override
	public void processCommand(ICommandSender ics, String[] args) throws CommandException
	{
		List<EntityPlayerMP> players = FTBLib.getAllOnlinePlayers(null);
		boolean printUUID = args.length > 0 && args[0].equals("uuid");
		
		FTBLib.printChat(ics, "Players currently online: [ " + players.size() + " ]");
		for(int i = 0; i < players.size(); i++)
		{
			EntityPlayerMP ep = players.get(i);
			
			if(printUUID)
				FTBLib.printChat(ics, ep.getCommandSenderName() + " :: " + LMUtils.fromUUID(ep.getUniqueID()));
			else FTBLib.printChat(ics, ep.getCommandSenderName());
		}
	}
}