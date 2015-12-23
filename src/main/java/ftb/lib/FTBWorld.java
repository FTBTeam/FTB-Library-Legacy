package ftb.lib;

import java.io.File;
import java.util.*;

import com.google.gson.*;

import cpw.mods.fml.relauncher.Side;
import ftb.lib.api.*;
import ftb.lib.mod.GameModesSerializer;
import ftb.lib.mod.net.MessageSendGameMode;
import latmod.lib.*;
import net.minecraft.world.World;

public class FTBWorld
{
	public static FTBWorld server = null, client = null;
	
	public static FTBWorld get(Side s)
	{ return s.isServer() ? server : client; }
	
	private final UUID worldID;
	private final String worldIDS;
	private String currentMode = "";
	
	private File currentModeFile = null;
	private File currentWorldIDFile = null;
	
	public FTBWorld(UUID id, String ids)
	{
		worldID = id;
		worldIDS = ids;
	}
	
	public FTBWorld(World w)
	{
		currentMode = "";
		try
		{
			currentModeFile = new File(w.getSaveHandler().getWorldDirectory(), "ftb_gamemode.txt");
			currentMode = LMFileUtils.loadAsText(currentModeFile).trim();
		}
		catch(Exception ex) { /*ex.printStackTrace();*/ }
		
		if(currentMode == null || currentMode.isEmpty())
			currentMode = gameModes.defaultMode;
		
		for(String s : gameModes.allModes)
			new File(FTBLib.folderModpack, s).mkdir();
		
		setMode(Side.SERVER, currentMode, true);
		
		FTBLib.logger.info("Current Mode: " + getMode());
		
		UUID worldID0 = null;
		try
		{
			currentWorldIDFile = new File(w.getSaveHandler().getWorldDirectory(), "ftb_worldID.dat");
			worldID0 = LMStringUtils.fromString(LMFileUtils.loadAsText(currentWorldIDFile).trim());
		}
		catch(Exception ex) { /*ex.printStackTrace();*/ }
		
		if(worldID0 == null)
		{
			worldID0 = UUID.randomUUID();
			try { LMFileUtils.save(currentWorldIDFile, LMStringUtils.fromUUID(worldID0)); }
			catch(Exception ex) { ex.printStackTrace(); }
		}
		
		worldID = worldID0;
		worldIDS = LMStringUtils.fromUUID(worldID);
	}
	
	/** 0 = OK, 1 - Mode is invalid, 2 - Mode already set (will be ignored and return 0, if forced == true) */
	public int setMode(Side side, String modeID, boolean forced)
	{
		if(modeID == null) return 1;
		modeID = modeID.trim();
		if(modeID.isEmpty() || modeID.equals(gameModes.commonMode)) return 1;
		if(!forced && !currentMode.isEmpty() && modeID.equals(currentMode)) return 2;
		if(!gameModes.allModes.contains(modeID)) return 1;
		currentMode = modeID;
		
		EventFTBModeSet event = new EventFTBModeSet(side, gameModes, currentMode);
		if(FTBLib.ftbu != null) FTBLib.ftbu.onModeSet(event);
		event.post();
		
		if(side.isServer())
		{
			try { LMFileUtils.save(currentModeFile, getMode()); }
			catch(Exception ex) { ex.printStackTrace(); }
			new MessageSendGameMode(currentMode).sendTo(null);
		}
		
		return 0;
	}
	
	public String getMode()
	{ return currentMode.isEmpty() ? gameModes.defaultMode : currentMode; }
	
	public File getCurrentModeFolder()
	{
		File f = new File(FTBLib.folderModpack, getMode());
		if(!f.exists()) f.mkdirs();
		return f;
	}
	
	public File getCurrentModeFile(String path)
	{ return new File(getCurrentModeFolder(), path); }
	
	public File getCommonModeFolder()
	{
		File f = new File(FTBLib.folderModpack, gameModes.commonMode);
		if(!f.exists()) f.mkdirs();
		return f;
	}
	
	public File getCommonModeFile(String path)
	{ return new File(getCommonModeFolder(), path); }
	
	public UUID getWorldID()
	{ return worldID; }
	
	public String getWorldIDS()
	{ return worldIDS; }
	
	// Static //
	
	private static File gamemodesJsonFile = null;
	private static GameModes gameModes = null;
	private static Gson gameModesGson = null;
	
	public static void init()
	{
		GsonBuilder gb = new GsonBuilder();
		gb.setPrettyPrinting();
		gb.registerTypeAdapter(GameModes.class, new GameModesSerializer());
		gameModesGson = gb.create();
		reloadGameModes();
	}
	
	public static void reloadGameModes()
	{
		if(gamemodesJsonFile == null) gamemodesJsonFile = LMFileUtils.newFile(new File(FTBLib.folderModpack, "gamemodes.json"));
		gameModes = LMJsonUtils.fromJsonFile(gameModesGson, gamemodesJsonFile, GameModes.class);
		if(gameModes == null)
		{
			List<String> list = new FastList<String>();
			list.add("default");
			list = Collections.unmodifiableList(list);
			gameModes = new GameModes(list, list.get(0), "common", new HashMap<String, String>());
			LMJsonUtils.toJsonFile(gameModesGson, gamemodesJsonFile, gameModes);
		}
	}
	
	public static GameModes getAllModes()
	{ return gameModes; }
}