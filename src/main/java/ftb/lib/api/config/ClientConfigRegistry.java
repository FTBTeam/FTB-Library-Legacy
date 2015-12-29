package ftb.lib.api.config;

import ftb.lib.FTBLib;
import ftb.lib.mod.net.MessageReload;
import latmod.lib.LMFileUtils;
import latmod.lib.config.*;
import net.minecraft.client.resources.I18n;

import java.io.File;

public final class ClientConfigRegistry
{
	private static final ConfigGroup group = new ConfigGroup("client_config");
	
	public static final IConfigProvider provider = new IConfigProvider()
	{
		public String getGroupTitle(ConfigGroup g)
		{ return I18n.format(g.getFullID()); }
		
		public String getEntryTitle(ConfigEntry e)
		{ return I18n.format(e.getFullID()); }
		
		public ConfigGroup getGroup()
		{ return group; }
		
		public void save()
		{
			if(group.parentFile == null) init();
			group.parentFile.save();
			MessageReload.reloadClient();
		}
	};
	
	public static void init()
	{
		File file = new File(FTBLib.folderLocal, "client/config.txt");
		if(file.exists()) LMFileUtils.delete(file); // TODO: Remove me
		
		file = LMFileUtils.newFile(new File(FTBLib.folderLocal, "client/config.json"));
		ConfigFile configFile = new ConfigFile(group, file);
		group.parentFile = configFile;
		configFile.load();
	}

	/** Do this before postInit() */
	public static void add(ConfigGroup g)
	{ group.add(g, false); }
}