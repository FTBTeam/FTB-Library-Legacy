package ftb.lib;

import java.io.File;

import com.google.common.collect.*;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import ftb.lib.api.*;
import ftb.lib.mod.*;
import latmod.lib.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class FTBGameModes
{
	private static GameModes gameModes = null;
	private static String currentMode = "";
	private static File currentModeFile = null;
	
	public static void playerLoggedIn(EntityPlayerMP ep)
	{ FTBLibNetHandler.NET.sendTo(new MessageSetMode(currentMode), ep); }
	
	public static void reloadGameModes()
	{
		System.out.println("Loading the list");
		
		GameModeList loadedList = LMJsonUtils.fromJsonFile(new File(FTBLib.folderModpack, "gamemodes.json"), GameModeList.class);
		if(loadedList == null) loadedList = new GameModeList();
		loadedList.setDefaults();
		
		gameModes = new GameModes(ImmutableSortedSet.copyOf(loadedList.modes), loadedList.defaultMode, loadedList.common, ImmutableSortedMap.copyOf(loadedList.custom));
		
		//
		
		System.out.println("Loading currentMode...");
		if(FMLCommonHandler.instance().getEffectiveSide().isClient() || MinecraftServer.getServer() == null) return;
		World w = MinecraftServer.getServer().getEntityWorld();
		if(w == null) return;
		
		currentMode = "";
		try
		{
			currentModeFile = new File(w.getSaveHandler().getWorldDirectory(), "ftb_gamemode.txt");
			currentMode = LMFileUtils.loadAsText(currentModeFile).trim();
		}
		catch(Exception ex) { ex.printStackTrace(); }
		
		if(currentMode == null || currentMode.isEmpty())
			currentMode = gameModes.defaultMode;
		FTBLib.logger.info("");
		
		GameModeList list = new GameModeList();
		list.setDefaults();
		list.defaultMode = gameModes.defaultMode;
		list.common = gameModes.commonMode;
		list.modes = gameModes.allModes.asList();
		list.custom = gameModes.customData;
		LMJsonUtils.toJsonFile(new File(FTBLib.folderModpack, "gamemodes.json"), list);
		
		for(String s : gameModes.allModes)
			new File(FTBLib.folderModpack, s).mkdir();
		
		setMode(Side.SERVER, currentMode, true);
		
		System.out.println("Current Mode: " + getMode());
	}
	
	/** 0 = OK, 1 - Mode is invalid, 2 - Mode already set (will be ignored and return 0, if forced == true) */
	public static int setMode(Side side, String modeID, boolean forced)
	{
		if(modeID == null) return 1;
		modeID = modeID.trim();
		if(modeID.isEmpty() || modeID.equals(gameModes.commonMode)) return 1;
		if(!forced && !currentMode.isEmpty() && modeID.equals(currentMode)) return 2;
		System.out.println("ModeID: " + modeID);
		if(!gameModes.allModes.contains(modeID)) return 1;
		currentMode = modeID;
		
		MinecraftForge.EVENT_BUS.post(new EventFTBModeSet(side, gameModes, currentMode));
		
		if(side.isServer())
		{
			try { LMFileUtils.save(currentModeFile, getMode()); }
			catch(Exception ex) { ex.printStackTrace(); }
			
			FTBLibNetHandler.NET.sendToAll(new MessageSetMode(currentMode));
		}
		
		return 0;
	}
	
	public static GameModes getAllModes()
	{ return gameModes; }
	
	public static String getMode()
	{ return currentMode.isEmpty() ? gameModes.defaultMode : currentMode; }
	
	public static File getCurrentModeFolder()
	{
		File f = new File(FTBLib.folderModpack, getMode());
		if(!f.exists()) f.mkdirs();
		return f;
	}
	
	public static File getCurrentModeFile(String path)
	{ return new File(getCurrentModeFolder(), path); }
	
	public static File getCommonModeFolder()
	{
		File f = new File(FTBLib.folderModpack, gameModes.commonMode);
		if(!f.exists()) f.mkdirs();
		return f;
	}
	
	public static File getCommonModeFile(String path)
	{ return new File(getCommonModeFolder(), path); }
}