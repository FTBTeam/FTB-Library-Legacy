package ftb.lib.mod;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;
import ftb.lib.*;
import ftb.lib.mod.net.FTBLibNetHandler;
import latmod.lib.OS;

@Mod(modid = FTBLibFinals.MOD_ID, name = FTBLibFinals.MOD_NAME, version = FTBLibFinals.VERSION, dependencies = FTBLibFinals.DEPS)
public class FTBLibMod
{
	@Mod.Instance(FTBLibFinals.MOD_ID)
	public static FTBLibMod inst;
	
	@SidedProxy(serverSide = "ftb.lib.mod.FTBLibModCommon", clientSide = "ftb.lib.mod.FTBLibModClient")
	public static FTBLibModCommon proxy;
	
	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent e)
	{
		if(FTBLibFinals.DEV)
			FTBLib.logger.info("Loading FTBLib, DevEnv");
		else
			FTBLib.logger.info("Loading FTBLib, v" + FTBLibFinals.VERSION);
		
		FTBLib.logger.info("OS: " + OS.current + ", 64bit: " + OS.is64);
		
		FTBLib.init(e.getModConfigurationDirectory());
		JsonHelper.init();
		FTBLibNetHandler.init();
		FTBWorld.init();
		
		EventBusHelper.register(new FTBLibEventHandler());
		proxy.preInit();
	}
	
	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent e)
	{ e.registerServerCommand(new CommandFTBWorld()); }
}