package ftb.lib.api.cmd;

import net.minecraft.command.CommandException;

public class MissingArgsException extends CommandException
{
	private static final long serialVersionUID = 1L;
	
	public MissingArgsException()
	{ super("ftbl:missing_args", new Object[0]); }
}