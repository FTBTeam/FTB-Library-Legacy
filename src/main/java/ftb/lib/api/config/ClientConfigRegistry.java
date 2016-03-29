package ftb.lib.api.config;

import cpw.mods.fml.relauncher.*;
import ftb.lib.FTBLib;
import net.minecraft.client.resources.I18n;

import java.io.File;

@SideOnly(Side.CLIENT)
public final class ClientConfigRegistry
{
	private static final ConfigFile file = new ConfigFile("client_config");
	
	public static IConfigProvider provider()
	{
		return new IConfigProvider()
		{
			public String getGroupTitle(ConfigGroup g)
			{ return I18n.format(g.getID()); }
			
			public String getEntryTitle(ConfigEntry e)
			{ return I18n.format(e.getFullID()); }
			
			public ConfigFile getConfigFile()
			{
				if(file.getFile() == null)
				{
					file.setFile(new File(FTBLib.folderLocal, "client/config.json"));
					file.load();
				}
				
				return file;
			}
			
			public void save()
			{ getConfigFile().save(); }
		};
	}
	
	/**
	 * Do this before postInit()
	 */
	public static void addGroup(String id, Class<?> c)
	{ file.addGroup(id, c); }
	
	public static void add(ConfigGroup group)
	{ file.add(group, false); }
}