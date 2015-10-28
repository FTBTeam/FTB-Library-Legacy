package ftb.lib.mod;

import ftb.lib.FTBWorld;
import net.minecraft.command.*;
import net.minecraft.util.ChatComponentTranslation;

public class CommandFTBWorldID extends CommandBase
{
	public String getCommandName()
	{ return "ftb_worldID"; }
	
	public String getCommandUsage(ICommandSender ics)
	{ return "/ftb_worldID"; }
	
	public int getRequiredPermissionLevel()
	{ return 0; }
	
	public void processCommand(ICommandSender ics, String[] args)
	{ ics.addChatMessage(new ChatComponentTranslation("ftbl:worldID", FTBWorld.server.getWorldIDS())); }
}