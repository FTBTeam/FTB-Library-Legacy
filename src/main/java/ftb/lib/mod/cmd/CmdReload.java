package ftb.lib.mod.cmd;

import ftb.lib.FTBLib;
import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.cmd.CommandLevel;
import ftb.lib.mod.config.FTBLibConfigCmdNames;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class CmdReload extends CommandLM
{
	public CmdReload()
	{ super(FTBLibConfigCmdNames.reload.getAsString(), CommandLevel.OP); }
	
	@Override
	public void processCommand(ICommandSender ics, String[] args) throws CommandException
	{
		FTBLib.reload(ics, true, args.length > 0 && args[0].equalsIgnoreCase("client"));
	}
}