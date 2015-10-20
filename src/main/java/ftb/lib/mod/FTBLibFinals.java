package ftb.lib.mod;

import cpw.mods.fml.common.Loader;

public class FTBLibFinals
{
	public static final String MOD_ID = "FTBL";
	public static final String MOD_NAME = "FTBLib";
	public static final String VERSION = "@VERSION@";
	public static final String DEPS = "required-after:Forge@[10.13.4.1448,);after:Baubles;;after:NotEnoughItems;after:Waila";
	
	public static final String MC_VERSION = Loader.MC_VERSION;
	public static final boolean DEV = VERSION.indexOf('@') > 0;
}