package ftb.lib.mod.cmd;

import ftb.lib.FTBLib;
import ftb.lib.api.cmd.*;
import ftb.lib.mod.config.FTBLibConfigCmd;
import net.minecraft.command.*;

public class CmdReload extends CommandLM
{
	public CmdReload()
	{ super(FTBLibConfigCmd.reload_name.get(), CommandLevel.OP); }
	
	public void processCommand(ICommandSender ics, String[] args) throws CommandException
	{
		FTBLib.reload(ics, true, false);
	}
}