package ftb.lib.api.cmd;

import ftb.lib.api.FTBLibLang;
import net.minecraft.command.CommandException;

public class MissingArgsException extends CommandException
{
	private static final long serialVersionUID = 1L;
	
	public MissingArgsException()
	{ super(FTBLibLang.missing_args.getID(), new Object[0]); }
}