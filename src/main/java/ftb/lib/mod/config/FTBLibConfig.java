package ftb.lib.mod.config;

import java.io.File;

import ftb.lib.FTBLib;
import ftb.lib.api.config.ConfigListRegistry;
import latmod.lib.config.ConfigFile;

public class FTBLibConfig
{
	private static ConfigFile configFile;
	
	public static void load()
	{
		configFile = new ConfigFile("ftblib", new File(FTBLib.folderLocal, "FTBLib.json"));
		configFile.configList.setName("FTBLib");
		configFile.add(FTBLibConfigCmd.group.addAll(FTBLibConfigCmd.class));
		ConfigListRegistry.instance.add(configFile);
		configFile.load();
	}
}