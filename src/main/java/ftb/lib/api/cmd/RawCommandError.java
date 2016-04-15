package ftb.lib.api.cmd;

import ftb.lib.mod.FTBLibLang;
import net.minecraft.command.CommandException;

public class RawCommandError extends CommandException
{
	public RawCommandError(Object o)
	{ super(FTBLibLang.raw.getID(), String.valueOf(o)); }
}