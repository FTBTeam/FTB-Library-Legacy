package ftb.lib.api.cmd;

import ftb.lib.mod.FTBLibLang;
import net.minecraft.command.CommandException;

public class FeatureDisabledException extends CommandException
{
	public FeatureDisabledException()
	{ super(FTBLibLang.feature_disabled.getID(), new Object[0]); }
}