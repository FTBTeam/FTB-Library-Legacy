package ftb.lib.mod.config;

import ftb.lib.FTBLib;
import ftb.lib.api.config.ConfigRegistry;
import latmod.lib.config.*;

import java.io.File;

public class FTBLibConfig
{
	private static ConfigFile configFile;
	
	public static void load()
	{
		configFile = new ConfigFile("ftblib", new File(FTBLib.folderLocal, "ftblib.json"));
		configFile.configGroup.setName("FTBLib");
		configFile.add(new ConfigGroup("commands").addAll(FTBLibConfigCmd.class, null, false));
		FTBLibConfigCmd.name.addAll(FTBLibConfigCmd.Name.class, null, false);
		
		ConfigRegistry.add(configFile);
		configFile.load();
	}
}