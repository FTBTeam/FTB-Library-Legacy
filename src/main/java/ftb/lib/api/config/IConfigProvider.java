package ftb.lib.api.config;

import latmod.lib.config.*;

public interface IConfigProvider
{
	String getGroupTitle(ConfigGroup g);
	String getEntryTitle(ConfigEntry e);
	ConfigGroup getGroup();
	void save();
	void closed(boolean changed);
}