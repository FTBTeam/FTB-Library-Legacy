package ftb.lib.api.cmd;

import net.minecraft.command.CommandException;

public class MissingArgsException extends CommandException
{
	public MissingArgsException()
	{ super("ftbl.missing_args"); }
}