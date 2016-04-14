package ftb.lib.api.cmd;

import ftb.lib.mod.FTBLibLang;
import net.minecraft.command.CommandException;

public class MissingArgsException extends CommandException
{
	public MissingArgsException()
	{ super(FTBLibLang.missing_args.getID(), new Object[0]); }
}