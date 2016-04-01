package ftb.lib.mod.cmd;

import ftb.lib.FTBLib;
import ftb.lib.api.cmd.*;
import ftb.lib.mod.config.FTBLibConfigCmdNames;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;

public class CmdReload extends CommandLM
{
	public CmdReload()
	{ super(FTBLibConfigCmdNames.reload.get(), CommandLevel.OP); }
	
	public IChatComponent onCommand(ICommandSender ics, String[] args)
	{
		FTBLib.reload(ics, true, args.length > 0 && args[0].equalsIgnoreCase("client"));
		return null;
	}
}