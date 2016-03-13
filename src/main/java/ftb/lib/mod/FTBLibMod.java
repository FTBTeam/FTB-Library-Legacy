package ftb.lib.mod;

import ftb.lib.*;
import ftb.lib.api.*;
import ftb.lib.api.config.ConfigRegistry;
import ftb.lib.api.item.ODItems;
import ftb.lib.api.permissions.ForgePermissionRegistry;
import ftb.lib.mod.cmd.*;
import ftb.lib.mod.config.*;
import ftb.lib.mod.net.FTBLibNetHandler;
import latmod.lib.util.OS;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.*;

import java.io.File;
import java.util.Map;

@Mod(modid = FTBLibFinals.MOD_ID, name = FTBLibFinals.MOD_NAME, version = FTBLibFinals.MOD_VERSION, dependencies = FTBLibFinals.MOD_DEP, acceptedMinecraftVersions = "[1.8.8, 1.9)")
public class FTBLibMod
{
	@Mod.Instance(FTBLibFinals.MOD_ID)
	public static FTBLibMod inst;
	
	@SidedProxy(serverSide = "ftb.lib.mod.FTBLibModCommon", clientSide = "ftb.lib.mod.client.FTBLibModClient")
	public static FTBLibModCommon proxy;
	
	public static final Logger logger = LogManager.getLogger("FTBLib");
	public static LMMod mod;
	
	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent e)
	{
		if(FTBLib.DEV_ENV) logger.info("Loading FTBLib, DevEnv");
		else logger.info("Loading FTBLib, v" + FTBLibFinals.MOD_VERSION);
		
		logger.info("OS: " + OS.current + ", 64bit: " + OS.is64);
		
		mod = LMMod.create(FTBLibFinals.MOD_ID);
		
		FTBLib.init(e.getModConfigurationDirectory());
		JsonHelper.init();
		FTBLibNetHandler.init();
		ODItems.preInit();
		
		FTBLibConfig.load();
		EventBusHelper.register(FTBLibEventHandler.instance);
		ForgePermissionRegistry.register(FTBLibPermissions.class);
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
		FTBLibEventHandler.instance.ticking.clear();
		
		if(FTBLibConfigCmd.override_list.get()) FTBLib.addCommand(e, new CmdListOverride());
		if(FTBLibConfigCmd.override_help.get()) FTBLib.addCommand(e, new CmdHelpOverride());
		if(!FTBLibConfigCmd.reload_name.get().isEmpty()) FTBLib.addCommand(e, new CmdReload());
		if(FTBLibConfigCmd.set_item_name.get()) FTBLib.addCommand(e, new CmdSetItemName());
		if(FTBLibConfigCmd.heal.get()) FTBLib.addCommand(e, new CmdHeal());
		if(FTBLibConfigCmd.edit_config.get()) FTBLib.addCommand(e, new CmdEditConfig());
		FTBLib.addCommand(e, new CmdMode());
		FTBLib.addCommand(e, new CmdNotify());
		FTBLib.addCommand(e, new CmdInv());
	}
	
	@Mod.EventHandler
	public void onServerAboutToStart(FMLServerAboutToStartEvent e)
	{
		ConfigRegistry.reload();
		GameModes.reload();
		ForgeWorldMP.inst = new ForgeWorldMP(new File(FMLCommonHandler.instance().getSavesDirectory(), e.getServer().getFolderName() + "/LatMod"));
		FTBLib.reload(FTBLib.getServer(), false, false);
		ForgeWorldMP.inst.init();
	}
	
	/*
	@Mod.EventHandler
	public void onServerStarted(FMLServerStartedEvent e)
	{
	}
	*/
	
	@Mod.EventHandler
	public void onServerShutDown(FMLServerStoppedEvent e)
	{
		ForgeWorldMP.inst.onClosed();
		ForgeWorldMP.inst = null;
		FTBLibEventHandler.instance.ticking.clear();
	}
	
	@NetworkCheckHandler
	public boolean checkNetwork(Map<String, String> m, Side side)
	{
		String s = m.get(FTBLibFinals.MOD_ID);
		return s == null || s.equals(FTBLibFinals.MOD_VERSION);
	}
}