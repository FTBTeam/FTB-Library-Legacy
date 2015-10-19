package ftb.lib.mod;

import java.io.*;

import com.google.common.collect.*;
import com.google.gson.*;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import ftb.lib.MessageSetMode;
import ftb.lib.api.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class FTBLib
{
	private static final Gson gamemodesGson = new GsonBuilder().setPrettyPrinting().create();
	
	public static File folderConfig;
	public static File folderMinecraft;
	public static File folderModpack;
	public static File folderLocal;
	
	private static GameModes gameModes = null;
	private static String currentMode = "";
	private static File currentModeFile = null;
	private static File gamemodesJson = null;
	
	public static void init(File c)
	{
		folderConfig = c;
		folderMinecraft = c.getParentFile();
		folderModpack = new File(folderMinecraft, "modpack/");
		folderLocal = new File(folderMinecraft, "local/");
		
		if(!folderModpack.exists()) folderModpack.mkdirs();
		if(!folderLocal.exists()) folderLocal.mkdirs();
		
		gamemodesJson = new File(folderModpack, "gamemodes.json");
	}
	
	public static void playerLoggedIn(EntityPlayerMP ep)
	{ FTBLibNetHandler.NET.sendTo(new MessageSetMode(currentMode), ep); }
	
	public static void reloadGameModes()
	{
		System.out.println("Loading the list");
		
		GameModeList loadedList = null;
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(gamemodesJson));
			loadedList = gamemodesGson.fromJson(reader, GameModeList.class);
			reader.close();
		}
		catch(Exception ex)
		{ ex.printStackTrace(); }
		
		if(loadedList == null) loadedList = new GameModeList();
		loadedList.setDefaults();
		
		gameModes = new GameModes(ImmutableSortedSet.copyOf(loadedList.modes), loadedList.defaultMode, loadedList.common, ImmutableSortedMap.copyOf(loadedList.custom));
		
		//
		
		if(FMLCommonHandler.instance().getEffectiveSide().isClient() || MinecraftServer.getServer() == null) return;
		World w = MinecraftServer.getServer().getEntityWorld();
		if(w == null) return;
		
		String currentMode = "";
		
		try
		{
			currentModeFile = new File(w.getSaveHandler().getWorldDirectory(), "ftb_gamemode.txt");
			if(!currentModeFile.exists()) currentModeFile.createNewFile();
			BufferedReader reader = new BufferedReader(new FileReader(currentModeFile));
			if(reader.ready())
			{
				try { currentMode = reader.readLine().trim(); }
				catch(Exception ex1) { }
			}
			reader.close();
		}
		catch(Exception ex)
		{ ex.printStackTrace(); }
		
		if(currentMode == null || currentMode.isEmpty())
			currentMode = gameModes.defaultMode;
		
		saveList();
		setMode(Side.SERVER, currentMode, true, true);
	}
	
	private static void saveList()
	{
		try
		{
			File file = new File(folderModpack, "gamemodes.json");
			if(!file.exists()) file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			
			GameModeList loadedList = new GameModeList();
			loadedList.setDefaults();
			
			loadedList.defaultMode = gameModes.defaultMode;
			loadedList.common = gameModes.commonMode;
			loadedList.modes = gameModes.allModes.asList();
			loadedList.custom = gameModes.customData;
			
			gamemodesGson.toJson(loadedList, GameModeList.class, writer);
			writer.close();
		}
		catch(Exception ex)
		{ ex.printStackTrace(); }
	}
	
	private static void saveGameModeFile()
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(currentModeFile));
			writer.write(currentMode);
			writer.close();
		}
		catch(Exception ex)
		{ ex.printStackTrace(); }
	}
	
	/** 0 = OK, 1 - Mode is invalid, 2 - Mode already set */
	public static int setMode(Side side, String modeID, boolean forced, boolean saveFile)
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
			FTBLibNetHandler.NET.sendToAll(new MessageSetMode(currentMode));
			if(saveFile) saveGameModeFile();
		}
		
		return 0;
	}
	
	public static GameModes getAllModes()
	{ return gameModes; }
	
	public static String getMode()
	{ return currentMode; }
	
	public static File getCurrentModeFolder()
	{
		if(currentMode.isEmpty()) return null;
		File f = new File(folderModpack, currentMode);
		if(!f.exists()) f.mkdirs();
		return f;
	}
	
	public static File getCurrentModeFile(String path)
	{ return new File(getCurrentModeFolder(), path); }
	
	public static File getCommonModeFolder()
	{
		File f = new File(folderModpack, gameModes.commonMode);
		if(!f.exists()) f.mkdirs();
		return f;
	}
	
	public static File getCommonModeFile(String path)
	{ return new File(getCommonModeFolder(), path); }
}