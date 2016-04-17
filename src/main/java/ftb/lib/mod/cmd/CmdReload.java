package ftb.lib.mod.cmd;

import ftb.lib.FTBLib;
import ftb.lib.api.cmd.*;
import ftb.lib.mod.config.FTBLibConfigCmd;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;

public class CmdReload extends CommandLM
{
	public CmdReload()
	{ super(FTBLibConfigCmd.reload_name.getAsString(), CommandLevel.OP); }
	
	public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
	{
		FTBLib.reload(ics, true, false);
	}
}