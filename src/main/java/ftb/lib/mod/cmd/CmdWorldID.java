package ftb.lib.mod.cmd;

import ftb.lib.FTBWorld;
import ftb.lib.api.cmd.*;
import ftb.lib.mod.config.FTBLibConfigCmd;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.*;

public class CmdWorldID extends CommandLM
{
	public CmdWorldID()
	{ super(FTBLibConfigCmd.Name.world_id.get(), CommandLevel.ALL); }
	
	public IChatComponent onCommand(ICommandSender ics, String[] args)
	{ return new ChatComponentTranslation("ftbl:worldID", FTBWorld.server.getWorldIDS()); }
}