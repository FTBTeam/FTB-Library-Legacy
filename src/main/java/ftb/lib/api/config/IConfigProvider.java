package ftb.lib.api.config;

import ftb.lib.api.config.old.*;

//@SideOnly(Side.CLIENT)
public interface IConfigProvider
{
	String getGroupTitle(ConfigGroup g);
	String getEntryTitle(ConfigEntry e);
	ConfigFile getConfigFile();
	void save();
}