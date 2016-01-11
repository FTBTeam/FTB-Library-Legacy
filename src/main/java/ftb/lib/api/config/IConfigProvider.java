package ftb.lib.api.config;

import latmod.lib.config.*;

//@SideOnly(Side.CLIENT)
public interface IConfigProvider
{
	String getGroupTitle(ConfigGroup g);
	String getEntryTitle(ConfigEntry e);
	ConfigGroup getGroup();
	void save();
}