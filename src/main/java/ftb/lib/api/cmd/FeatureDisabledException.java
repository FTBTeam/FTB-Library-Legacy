package ftb.lib.api.cmd;

import net.minecraft.command.CommandException;

public class FeatureDisabledException extends CommandException
{
	public FeatureDisabledException()
	{ super("ftbl.feature_disabled"); }
}