package ftb.lib.mod.cmd;

import ftb.lib.FTBLib;
import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;
import ftb.lib.mod.config.FTBLibConfigCmd;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CmdReload extends CommandLM
{
	public CmdReload()
	{ super(FTBLibConfigCmd.reload_name.getAsString(), CommandLevel.OP); }
	
	@Override
	public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
	{
		FTBLib.reload(ics, true, false);
	}
}