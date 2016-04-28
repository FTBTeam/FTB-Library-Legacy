package ftb.lib.mod;

import ftb.lib.EventBusHelper;
import ftb.lib.FTBLib;
import ftb.lib.JsonHelper;
import ftb.lib.LMMod;
import ftb.lib.api.ForgeWorldMP;
import ftb.lib.api.GameModes;
import ftb.lib.api.config.ConfigRegistry;
import ftb.lib.api.item.ODItems;
import ftb.lib.api.permissions.ForgePermissionRegistry;
import ftb.lib.mod.cmd.CmdEditConfig;
import ftb.lib.mod.cmd.CmdHeal;
import ftb.lib.mod.cmd.CmdHelpOverride;
import ftb.lib.mod.cmd.CmdInv;
import ftb.lib.mod.cmd.CmdListOverride;
import ftb.lib.mod.cmd.CmdMode;
import ftb.lib.mod.cmd.CmdNotify;
import ftb.lib.mod.cmd.CmdReload;
import ftb.lib.mod.cmd.CmdSetItemName;
import ftb.lib.mod.config.FTBLibConfig;
import ftb.lib.mod.config.FTBLibConfigCmd;
import ftb.lib.mod.net.FTBLibNetHandler;
import latmod.lib.util.OS;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Map;

@Mod(modid = FTBLibFinals.MOD_ID, name = FTBLibFinals.MOD_NAME, version = FTBLibFinals.MOD_VERSION, dependencies = FTBLibFinals.MOD_DEP, acceptedMinecraftVersions = "[1.9, 1.10)")
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
		FTBLibEventHandler.ticking.clear();
		
		FTBLib.addCommand(e, new CmdReload());
		FTBLib.addCommand(e, new CmdMode());
		FTBLib.addCommand(e, new CmdNotify());
		FTBLib.addCommand(e, new CmdInv());
		
		if(FTBLibConfigCmd.override_list.getAsBoolean()) FTBLib.addCommand(e, new CmdListOverride());
		if(FTBLibConfigCmd.override_help.getAsBoolean()) FTBLib.addCommand(e, new CmdHelpOverride());
		if(FTBLibConfigCmd.set_item_name.getAsBoolean()) FTBLib.addCommand(e, new CmdSetItemName());
		if(FTBLibConfigCmd.heal.getAsBoolean()) FTBLib.addCommand(e, new CmdHeal());
		if(FTBLibConfigCmd.edit_config.getAsBoolean()) FTBLib.addCommand(e, new CmdEditConfig());
	}
	
	@Mod.EventHandler
	public void onServerStarted(FMLServerAboutToStartEvent e)
	{
		ConfigRegistry.reload();
		GameModes.reload();
		
		ForgeWorldMP.inst = new ForgeWorldMP(new File(FMLCommonHandler.instance().getSavesDirectory(), e.getServer().getFolderName() + "/LatMod/"));
		ForgeWorldMP.inst.init();
		
		try
		{
			ForgeWorldMP.inst.load();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	@Mod.EventHandler
	public void onServerStarted(FMLServerStartedEvent e)
	{
		FTBLib.reload(FTBLib.getServer(), false, false);
	}
	
	@Mod.EventHandler
	public void onServerShutDown(FMLServerStoppedEvent e)
	{
		ForgeWorldMP.inst.onClosed();
		ForgeWorldMP.inst = null;
		FTBLibEventHandler.ticking.clear();
	}
	
	@NetworkCheckHandler
	public boolean checkNetwork(Map<String, String> m, Side side)
	{
		String s = m.get(FTBLibFinals.MOD_ID);
		return s == null || s.equals(FTBLibFinals.MOD_VERSION);
	}
}