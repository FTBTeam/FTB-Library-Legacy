package ftb.lib.mod.cmd;

import ftb.lib.cmd.*;
import ftb.lib.mod.FTBLibMod;
import ftb.lib.mod.config.FTBLibConfigCmd;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;

public class CmdReload extends CommandLM
{
	public CmdReload()
	{ super(FTBLibConfigCmd.name_reload.get(), CommandLevel.OP); }

	public IChatComponent onCommand(ICommandSender ics, String[] args)
	{ FTBLibMod.reload(ics, true); return null; }
}