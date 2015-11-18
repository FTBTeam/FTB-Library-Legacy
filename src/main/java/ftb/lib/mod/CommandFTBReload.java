package ftb.lib.mod;

import ftb.lib.cmd.*;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;

public class CommandFTBReload extends CommandLM
{
	public CommandFTBReload()
	{ super("ftb_reload", CommandLevel.OP); }

	public IChatComponent onCommand(ICommandSender ics, String[] args)
	{ FTBLibMod.reload(ics, true); return null; }
}