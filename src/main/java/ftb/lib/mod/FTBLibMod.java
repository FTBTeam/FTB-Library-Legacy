package ftb.lib.mod;

import ftb.lib.*;
import ftb.lib.api.GameModes;
import ftb.lib.api.config.ConfigRegistry;
import ftb.lib.api.item.ODItems;
import ftb.lib.api.players.*;
import ftb.lib.mod.cmd.*;
import ftb.lib.mod.config.*;
import ftb.lib.mod.net.FTBLibNetHandler;
import latmod.lib.util.OS;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.Map;

@Mod(modid = FTBLibFinals.MOD_ID, name = FTBLibFinals.MOD_NAME, version = FTBLibFinals.MOD_VERSION, dependencies = FTBLibFinals.MOD_DEP, acceptedMinecraftVersions = "[1.8.8, 1.9)")
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
		if(FTBLib.DEV_ENV) FTBLib.logger.info("Loading FTBLib, DevEnv");
		else FTBLib.logger.info("Loading FTBLib, v" + FTBLibFinals.MOD_VERSION);
		
		FTBLib.logger.info("OS: " + OS.current + ", 64bit: " + OS.is64);
		
		mod = LMMod.create(FTBLibFinals.MOD_ID);
		
		FTBLib.init(e.getModConfigurationDirectory());
		JsonHelper.init();
		FTBLibNetHandler.init();
		ODItems.preInit();
		
		FTBLibConfig.load();
		EventBusHelper.register(FTBLibEventHandler.instance);
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
		FTBLib.addCommand(e, new CmdEditConfig());
		FTBLib.addCommand(e, new CmdMode());
		FTBLib.addCommand(e, new CmdReload());
		FTBLib.addCommand(e, new CmdNotify());
		FTBLib.addCommand(e, new CmdSetItemName());
		FTBLib.addCommand(e, new CmdInv());
	}
	
	/*
	@Mod.EventHandler
	public void onServerAboutToStart(FMLServerAboutToStartEvent e)
	{
	}
	*/
	
	@Mod.EventHandler
	public void onServerStarted(FMLServerStartedEvent e)
	{
		ConfigRegistry.reload();
		GameModes.reload();
		ForgeWorldMP.inst = new ForgeWorldMP(new File(new File(FMLCommonHandler.instance().getSavesDirectory(), FTBLib.getServer().getFolderName()), "LatMod"));
		FTBLib.reload(FTBLib.getServer(), false, false);
		
		for(ForgeWorldData d : ForgeWorldMP.inst.customData.values())
		{
			if(d instanceof IWorldTick)
			{
				FTBLibEventHandler.instance.ticking.add((IWorldTick) d);
			}
			
			d.init();
		}
	}
	
	@Mod.EventHandler
	public void onServerShutDown(FMLServerStoppedEvent e)
	{
		ForgeWorldMP.inst.onClosed();
		ForgeWorldMP.inst = null;
	}
	
	@NetworkCheckHandler
	public boolean checkNetwork(Map<String, String> m, Side side)
	{
		String s = m.get(FTBLibFinals.MOD_ID);
		return s == null || s.equals(FTBLibFinals.MOD_VERSION);
	}
}