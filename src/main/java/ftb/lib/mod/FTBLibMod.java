package ftb.lib.mod;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;
import ftb.lib.*;
import ftb.lib.api.EventFTBWorldServer;
import ftb.lib.api.config.ConfigRegistry;
import ftb.lib.item.ODItems;
import ftb.lib.mod.cmd.*;
import ftb.lib.mod.config.*;
import ftb.lib.mod.net.FTBLibNetHandler;
import latmod.lib.util.OS;

import java.io.File;

@Mod(modid = FTBLibFinals.MOD_ID, name = FTBLibFinals.MOD_NAME, version = FTBLibFinals.VERSION, dependencies = FTBLibFinals.DEPS)
public class FTBLibMod
{
	@Mod.Instance(FTBLibFinals.MOD_ID)
	public static FTBLibMod inst;
	
	@SidedProxy(serverSide = "ftb.lib.mod.FTBLibModCommon", clientSide = "ftb.lib.mod.client.FTBLibModClient")
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
		ODItems.preInit();
		
		FTBLibConfig.load();
		EventBusHelper.register(new FTBLibEventHandler());
		proxy.preInit();
	}
	
	@Mod.EventHandler
	public void onPostInit(FMLPostInitializationEvent e)
	{
		ODItems.postInit();
		proxy.postInit();
	}
	
	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent e)
	{
		if(FTBLibConfigCmd.override_list.get()) e.registerServerCommand(new CmdListOverride());
		if(FTBLibConfigCmd.override_help.get()) e.registerServerCommand(new CmdHelpOverride());
		e.registerServerCommand(new CmdEditConfig());
		e.registerServerCommand(new CmdMode());
		e.registerServerCommand(new CmdReload());
		e.registerServerCommand(new CmdWorldID());
		e.registerServerCommand(new CmdNotify());
		e.registerServerCommand(new CmdSetItemName());
	}

	@Mod.EventHandler
	public void onServerAboutToStart(FMLServerAboutToStartEvent e)
	{
		FTBLib.folderWorld = new File(FTBLib.folderMinecraft, FTBLib.getServer().getFolderName());

		ConfigRegistry.reload();
		FTBWorld.reloadGameModes();
		FTBWorld.server = new FTBWorld();
		EventFTBWorldServer event = new EventFTBWorldServer(FTBWorld.server, FTBLib.getServer());
		if(FTBLib.ftbu != null) FTBLib.ftbu.onFTBWorldServer(event);
		event.post();
		FTBLib.reload(FTBLib.getServer(), false, true);
	}

	@Mod.EventHandler
	public void onServerShutDown(FMLServerStoppedEvent e)
	{
		if(FTBLib.ftbu != null) FTBLib.ftbu.onFTBWorldServerClosed();
		FTBWorld.server = null;
		FTBLib.folderWorld = null;
	}
	
	/*
	@NetworkCheckHandler
	public boolean checkNetwork(Map<String, String> m, Side side)
	{
		String s = m.get(FTBLibFinals.MOD_ID);
		return s == null || FTBLibFinals.DEV || s.equals(FTBLibFinals.VERSION);
	}*/
}