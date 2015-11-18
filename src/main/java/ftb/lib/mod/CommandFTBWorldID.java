package ftb.lib.mod;

import ftb.lib.FTBWorld;
import ftb.lib.cmd.*;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.*;

public class CommandFTBWorldID extends CommandLM
{
	public CommandFTBWorldID()
	{ super("ftb_worldID", CommandLevel.ALL); }
	
	public IChatComponent onCommand(ICommandSender ics, String[] args)
	{ return new ChatComponentTranslation("ftbl:worldID", FTBWorld.server.getWorldIDS()); }
}