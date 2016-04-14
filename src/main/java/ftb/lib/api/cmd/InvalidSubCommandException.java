package ftb.lib.api.cmd;

import ftb.lib.mod.FTBLibLang;
import net.minecraft.command.CommandException;

/**
 * Created by LatvianModder on 23.02.2016.
 */
public class InvalidSubCommandException extends CommandException
{
	public InvalidSubCommandException(String s)
	{ super(FTBLibLang.invalid_subcmd.getID(), s); }
}
