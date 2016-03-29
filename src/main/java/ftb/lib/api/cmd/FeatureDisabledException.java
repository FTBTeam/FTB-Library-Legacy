package ftb.lib.api.cmd;

import ftb.lib.api.FTBLibLang;
import net.minecraft.command.CommandException;

public class FeatureDisabledException extends CommandException
{
	private static final long serialVersionUID = 1L;
	
	public FeatureDisabledException()
	{ super(FTBLibLang.feature_disabled.getID(), new Object[0]); }
}