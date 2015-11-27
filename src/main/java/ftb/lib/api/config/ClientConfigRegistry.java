package ftb.lib.api.config;

import java.io.File;

import cpw.mods.fml.relauncher.*;
import ftb.lib.FTBLib;
import latmod.lib.LMFileUtils;
import latmod.lib.config.*;
import net.minecraft.util.*;

public final class ClientConfigRegistry
{
	private static File file;
	public static ConfigFile configFile;
	
	@SideOnly(Side.CLIENT)
	public static final IConfigProvider provider = new IConfigProvider()
	{
		public IChatComponent getTitle()
		{ return new ChatComponentTranslation("config.client_config"); }
		
		public ConfigList getList()
		{ return configFile.configList; }
		
		public void save()
		{ configFile.save(); }
	};
	
	public static void init()
	{
		file = new File(FTBLib.folderLocal, "client/config.txt");
		if(file.exists()) LMFileUtils.delete(file); // TODO: Remove me
		
		file = LMFileUtils.newFile(new File(FTBLib.folderLocal, "client/config.json"));
		configFile = new ConfigFile("client_config", file, true);
	}
	
	public static void add(ConfigGroup g)
	{ configFile.add(g); }
}