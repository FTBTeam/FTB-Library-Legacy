package ftb.lib.api.config;

import ftb.lib.FTBLib;
import latmod.lib.config.*;
import net.minecraft.client.resources.I18n;

import java.io.File;

public final class ClientConfigRegistry
{
	private static final ConfigFile file = new ConfigFile("client_config");
	
	public static IConfigProvider provider()
	{
		return new IConfigProvider()
		{
			public String getGroupTitle(ConfigGroup g)
			{ return I18n.format(g.ID); }
			
			public String getEntryTitle(ConfigEntry e)
			{ return I18n.format(e.getFullID()); }
			
			public ConfigFile getConfigFile()
			{
				if(file.getFile() == null) init();
				return file;
			}
		};
	}
	
	public static void init()
	{
		file.setFile(new File(FTBLib.folderLocal, "client/config.json"));
		file.load();
	}
	
	/**
	 * Do this before postInit()
	 */
	public static void add(ConfigGroup g)
	{ file.add(g, false); }
}