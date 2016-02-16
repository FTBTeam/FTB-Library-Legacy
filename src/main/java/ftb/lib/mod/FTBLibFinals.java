package ftb.lib.mod;

import net.minecraft.launchwrapper.Launch;

public class FTBLibFinals
{
	public static final String MOD_ID = "FTBL";
	public static final String MOD_NAME = "FTBLib";
	public static final String MOD_VERSION = "@VERSION@";
	public static final String MOD_DEP = "required-after:Forge@[11.15.1.1722,);after:Baubles;after:JEI;after:Waila;after:MineTweaker3";
	public static final boolean DEV = ((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")).booleanValue();
}