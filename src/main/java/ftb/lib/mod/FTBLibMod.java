package ftb.lib.mod;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.*;
import ftb.lib.*;

@Mod(modid = FTBLibMod.MOD_ID, name = "FTBLib", version = "@VERSION@", dependencies = "required-after:Forge@[10.13.4.1448,)")
public class FTBLibMod
{
	public static final String MOD_ID = "FTBL";
	
	@Mod.Instance(MOD_ID)
	public static FTBLibMod inst;
	
	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent e)
	{
		FTBLib.init(e.getModConfigurationDirectory());
		EventBusHelper.register(new FTBLibEventHandler());
		FTBLibNetHandler.init();
	}
	
	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent e)
	{ e.registerServerCommand(new CommandFTBLMode()); }
}