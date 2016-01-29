package ftb.lib.mod;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;
import ftb.lib.*;
import ftb.lib.api.*;
import ftb.lib.api.cmd.CommandLM;
import ftb.lib.api.config.ConfigRegistry;
import ftb.lib.api.item.ODItems;
import ftb.lib.mod.cmd.*;
import ftb.lib.mod.config.*;
import ftb.lib.mod.net.FTBLibNetHandler;
import latmod.lib.util.OS;

import java.io.File;
import java.util.Map;

@Mod(modid = FTBLibFinals.MOD_ID, name = FTBLibFinals.MOD_NAME, version = FTBLibFinals.MOD_VERSION, dependencies = FTBLibFinals.MOD_DEP, acceptedMinecraftVersions = "1.7.10")
public class FTBLibMod
{
	@Mod.Instance(FTBLibFinals.MOD_ID)
	public static FTBLibMod inst;
	
	@SidedProxy(serverSide = "ftb.lib.mod.FTBLibModCommon", clientSide = "ftb.lib.mod.client.FTBLibModClient")
	public static FTBLibModCommon proxy;
	
	public static LMMod mod;
	
	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent e)
	{
		if(FTBLibFinals.DEV) FTBLib.logger.info("Loading FTBLib, DevEnv");
		else FTBLib.logger.info("Loading FTBLib, v" + FTBLibFinals.MOD_VERSION);
		
		FTBLib.logger.info("OS: " + OS.current + ", 64bit: " + OS.is64);
		
		mod = LMMod.create(FTBLibFinals.MOD_ID);
		
		FTBLib.init(e.getModConfigurationDirectory());
		JsonHelper.init();
		FTBLibNetHandler.init();
		ODItems.preInit();
		
		FTBLibConfig.load();
		EventBusHelper.register(new FTBLibEventHandler());
		proxy.preInit();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent e)
	{
		FMLInterModComms.sendMessage("Waila", "register", "ftb.lib.api.waila.EventRegisterWaila.registerHandlers");
	}
	
	@Mod.EventHandler
	public void onPostInit(FMLPostInitializationEvent e)
	{
		ODItems.postInit();
		proxy.postInit();
		GameModes.reload();
		ConfigRegistry.reload();
	}
	
	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent e)
	{
		if(FTBLibConfigCmd.override_list.get()) e.registerServerCommand(new CmdListOverride());
		if(FTBLibConfigCmd.override_help.get()) e.registerServerCommand(new CmdHelpOverride());
		addCmd(e, new CmdEditConfig());
		addCmd(e, new CmdMode());
		addCmd(e, new CmdReload());
		addCmd(e, new CmdNotify());
		addCmd(e, new CmdSetItemName());
		addCmd(e, new CmdTrashCan());
	}
	
	private void addCmd(FMLServerStartingEvent e, CommandLM c)
	{ if(!c.commandName.isEmpty()) e.registerServerCommand(c); }
	
	@Mod.EventHandler
	public void onServerAboutToStart(FMLServerAboutToStartEvent e)
	{
		FTBLib.folderWorld = new File(FMLCommonHandler.instance().getSavesDirectory(), FTBLib.getServer().getFolderName());
		ConfigRegistry.reload();
	}
	
	@Mod.EventHandler
	public void onServerAboutToStart(FMLServerStartedEvent e)
	{
		GameModes.reload();
		
		FTBWorld.server = new FTBWorld(FTBLib.getServerWorld());
		EventFTBWorldServer event = new EventFTBWorldServer(FTBWorld.server, FTBLib.getServer());
		if(FTBLib.ftbu != null) FTBLib.ftbu.onFTBWorldServer(event);
		event.post();
		
		FTBLib.reload(FTBLib.getServer(), false, false);
	}
	
	@Mod.EventHandler
	public void onServerShutDown(FMLServerStoppedEvent e)
	{
		if(FTBLib.ftbu != null) FTBLib.ftbu.onFTBWorldServerClosed();
		FTBWorld.server = null;
		FTBLib.folderWorld = null;
		LMAccessToken.clear();
		ConfigRegistry.clearTemp();
	}
	
	@NetworkCheckHandler
	public boolean checkNetwork(Map<String, String> m, Side side)
	{
		String s = m.get(FTBLibFinals.MOD_ID);
		return s == null || s.equals(FTBLibFinals.MOD_VERSION);
	}
}