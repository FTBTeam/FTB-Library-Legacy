package ftb.lib.api.config;

import java.io.File;

import cpw.mods.fml.relauncher.*;
import ftb.lib.FTBLib;
import latmod.lib.LMFileUtils;
import latmod.lib.config.*;
import net.minecraft.client.resources.I18n;

public final class ClientConfigRegistry
{
	public static final ConfigGroup group = new ConfigGroup("client_config");
	
	@SideOnly(Side.CLIENT)
	public static final IConfigProvider provider = new IConfigProvider()
	{
		public String getGroupTitle(ConfigGroup g)
		{ return I18n.format(g.getFullID()); }
		
		public String getEntryTitle(ConfigEntry e)
		{ return I18n.format(e.getFullID()); }
		
		public ConfigGroup getGroup()
		{ return group; }
		
		public void save()
		{ group.parentFile.save(); }
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
	
	public static void add(ConfigGroup g)
	{ group.add(g); provider.save(); }
}