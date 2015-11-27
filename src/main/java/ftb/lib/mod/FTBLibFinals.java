package ftb.lib.mod;

public class FTBLibFinals
{
	public static final String MOD_ID = "FTBL";
	public static final String MOD_NAME = "FTBLib";
	public static final String VERSION = "@VERSION@";
	public static final String MOD_ID_LC = MOD_ID.toLowerCase();
	public static final String ASSETS = MOD_ID_LC + ':';
	public static final String DEPS = "required-after:Forge@[10.13.4.1448,);after:Baubles;;after:NotEnoughItems;after:Waila";
	public static final boolean DEV = VERSION.indexOf('@') != -1;
}