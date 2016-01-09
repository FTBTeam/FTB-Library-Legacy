package ftb.lib.mod.cmd;

import ftb.lib.FTBLib;
import ftb.lib.cmd.*;
import ftb.lib.mod.config.FTBLibConfigCmd;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;

public class CmdReload extends CommandLM
{
	public CmdReload()
	{ super(FTBLibConfigCmd.Name.reload.get(), CommandLevel.OP); }

	public IChatComponent onCommand(ICommandSender ics, String[] args)
	{
		FTBLib.reload(ics, true, true);
		return null;
	}
}