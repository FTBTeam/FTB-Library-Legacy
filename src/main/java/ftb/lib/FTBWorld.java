package ftb.lib;

import cpw.mods.fml.relauncher.Side;
import ftb.lib.api.*;
import latmod.lib.*;
import latmod.lib.json.UUIDTypeAdapterLM;

import java.io.File;
import java.util.UUID;

public class FTBWorld
{
	public static FTBWorld server = null, client = null;
	
	public static FTBWorld get(Side s)
	{ return s.isServer() ? server : client; }
	
	public final Side side;
	private final UUID worldID;
	private final String worldIDS;
	private GameMode currentMode;
	
	private File currentModeFile = null;
	private File currentWorldIDFile = null;
	
	public FTBWorld(Side s, UUID id, String ids)
	{
		side = s;
		worldID = id;
		worldIDS = ids;
		currentMode = new GameMode("default");
	}
	
	public FTBWorld()
	{
		side = Side.SERVER;
		currentMode = GameModes.getGameModes().defaultMode;
		try
		{
			currentModeFile = new File(FTBLib.folderWorld, "ftb_gamemode.txt");
			currentMode = GameModes.getGameModes().get(LMFileUtils.loadAsText(currentModeFile).trim());
		}
		catch(Exception ex) { /*ex.printStackTrace();*/ }
		
		for(GameMode s : GameModes.getGameModes().modes.values()) s.getFolder();
		
		FTBLib.logger.info("Current Mode: " + currentMode);
		
		UUID worldID0 = null;
		try
		{
			currentWorldIDFile = new File(FTBLib.folderWorld, "ftb_worldID.dat");
			if(currentWorldIDFile.exists())
				worldID0 = UUIDTypeAdapterLM.getUUID(LMFileUtils.loadAsText(currentWorldIDFile).trim());
		}
		catch(Exception ex) { /*ex.printStackTrace();*/ }
		
		if(worldID0 == null)
		{
			worldID0 = UUID.randomUUID();
			try { LMFileUtils.save(currentWorldIDFile, UUIDTypeAdapterLM.getString(worldID0)); }
			catch(Exception ex) { ex.printStackTrace(); }
		}
		
		worldID = worldID0;
		worldIDS = UUIDTypeAdapterLM.getString(worldID);
	}
	
	public GameMode getMode()
	{ return currentMode; }
	
	public void writeReloadData(ByteIOStream io)
	{
		io.writeUTF(currentMode.ID);
	}
	
	public void readReloadData(ByteIOStream io)
	{
		String mode = io.readUTF();
		currentMode = GameModes.getGameModes().get(mode);
	}
	
	/**
	 * 0 = OK, 1 - Mode is invalid, 2 - Mode already set (will be ignored and return 0, if forced == true)
	 */
	public int setMode(String s)
	{
		GameMode m = GameModes.getGameModes().modes.get(s);
		
		if(m == null) return 1;
		if(m.equals(currentMode)) return 2;
		
		currentMode = m;
		
		if(side.isServer())
		{
			try { LMFileUtils.save(currentModeFile, currentMode.ID); }
			catch(Exception ex) { ex.printStackTrace(); }
		}
		
		return 0;
	}
	
	public UUID getWorldID()
	{ return worldID; }
	
	public String getWorldIDS()
	{ return worldIDS; }
}