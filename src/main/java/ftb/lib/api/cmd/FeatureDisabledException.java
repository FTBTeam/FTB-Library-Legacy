package ftb.lib.api.cmd;

import net.minecraft.command.CommandException;

public class FeatureDisabledException extends CommandException
{
	private static final long serialVersionUID = 1L;
	
	public FeatureDisabledException()
	{ super("ftbl:feature_disabled", new Object[0]); }
}