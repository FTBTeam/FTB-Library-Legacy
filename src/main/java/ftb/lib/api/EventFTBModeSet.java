package ftb.lib.api;

import cpw.mods.fml.relauncher.Side;
import ftb.lib.FTBLib;

import java.io.File;

public class EventFTBModeSet extends EventLM
{
	public final Side side;
	public final GameModes modes;
	public final String currentMode;
	
	public EventFTBModeSet(Side s, GameModes g, String c)
	{ side = s; modes = g; currentMode = c; }
	
	public File getFolder()
	{
		File f = new File(FTBLib.folderModpack, currentMode);
		if(!f.exists()) return f;
		return f;
	}
	
	public File getFile(String path)
	{ return new File(getFolder(), path); }
	
	public File getCommonFolder()
	{
		File f = new File(FTBLib.folderModpack, modes.commonMode);
		if(!f.exists()) return f;
		return f;
	}
	
	public File getCommonFile(String path)
	{ return new File(getCommonFolder(), path); }
}