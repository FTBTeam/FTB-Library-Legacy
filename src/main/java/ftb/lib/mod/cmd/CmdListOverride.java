package ftb.lib.mod.cmd;

import ftb.lib.FTBLib;
import ftb.lib.api.cmd.*;
import latmod.lib.LMUtils;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public class CmdListOverride extends CommandLM
{
	public CmdListOverride()
	{ super("list", CommandLevel.ALL); }
	
	public String getCommandUsage(ICommandSender ics)
	{ return '/' + commandName + " ['uuid']"; }
	
	public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
	{
		List<EntityPlayerMP> players = FTBLib.getAllOnlinePlayers(null);
		boolean printUUID = args.length > 0 && args[0].equals("uuid");
		
		FTBLib.printChat(ics, "Players currently online: [ " + players.size() + " ]");
		for(EntityPlayerMP ep : players)
		{
			if(printUUID) FTBLib.printChat(ics, ep.getName() + " :: " + LMUtils.fromUUID(ep.getUniqueID()));
			else FTBLib.printChat(ics, ep.getName());
		}
	}
}