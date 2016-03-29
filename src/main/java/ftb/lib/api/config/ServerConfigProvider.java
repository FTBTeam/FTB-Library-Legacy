package ftb.lib.api.config;

import ftb.lib.mod.net.MessageEditConfigResponse;

public class ServerConfigProvider implements IConfigProvider
{
	public final long adminToken;
	public final boolean reload;
	public final ConfigFile file;
	
	public ServerConfigProvider(long t, boolean r, ConfigFile f)
	{
		adminToken = t;
		reload = r;
		file = f;
	}
	
	public String getTitle()
	{ return file.getDisplayName(); }
	
	public String getGroupTitle(ConfigGroup g)
	{ return g.getID(); }
	
	public String getEntryTitle(ConfigEntry e)
	{ return e.getID(); }
	
	public ConfigFile getConfigFile()
	{ return file; }
	
	public void save()
	{ new MessageEditConfigResponse(this).sendToServer(); }
}