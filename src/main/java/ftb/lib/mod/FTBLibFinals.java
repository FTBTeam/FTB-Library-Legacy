package ftb.lib.mod;

public class FTBLibFinals
{
	public static final String MOD_ID = "FTBL";
	public static final String MOD_NAME = "FTBLib";
	public static final String VERSION = "@VERSION@";
	public static final String MOD_ID_LC = MOD_ID.toLowerCase();
	public static final String ASSETS = MOD_ID_LC + ':';
	public static final String DEPS = "required-after:Forge@[11.15.0.1699,);after:Baubles;;after:NotEnoughItems;after:Waila;after:MineTweaker3";
	public static final boolean DEV = VERSION.indexOf('@') != -1;
}