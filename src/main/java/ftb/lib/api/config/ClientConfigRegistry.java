package ftb.lib.api.config;

import java.io.File;

import cpw.mods.fml.relauncher.*;
import ftb.lib.FTBLib;
import latmod.lib.LMFileUtils;
import latmod.lib.config.*;
import net.minecraft.client.resources.I18n;

public final class ClientConfigRegistry
{
	private static File file;
	public static ConfigFile configFile;
	
	@SideOnly(Side.CLIENT)
	public static final IConfigProvider provider = new IConfigProvider()
	{
		public String getGroupTitle(ConfigGroup g)
		{ return I18n.format(g.getFullID()); }
		
		public String getEntryTitle(ConfigEntry e)
		{ return I18n.format(e.getFullID()); }
		
		public ConfigGroup getGroup()
		{ return configFile.configGroup; }
		
		public void save()
		{ configFile.save(); }
	};
	
	public static void init()
	{
		file = new File(FTBLib.folderLocal, "client/config.txt");
		if(file.exists()) LMFileUtils.delete(file); // TODO: Remove me
		
		file = LMFileUtils.newFile(new File(FTBLib.folderLocal, "client/config.json"));
		configFile = new ConfigFile("client_config", file);
	}
	
	public static void add(ConfigGroup g)
	{ configFile.add(g); }
}