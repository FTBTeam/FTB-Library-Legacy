package ftb.lib.api.cmd;

import net.minecraft.command.CommandException;

public class InvalidSubCommandException extends CommandException
{
	public InvalidSubCommandException(String s)
	{ super("ftbl.invalid_subcmd", s); }
}