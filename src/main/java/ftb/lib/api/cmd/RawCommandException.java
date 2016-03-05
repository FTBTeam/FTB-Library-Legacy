package ftb.lib.api.cmd;

import net.minecraft.command.CommandException;

/**
 * Created by LatvianModder on 04.03.2016.
 */
public class RawCommandException extends CommandException
{
	public RawCommandException(Object o)
	{
		super("ftbl.raw_string", String.valueOf(o));
	}
}
