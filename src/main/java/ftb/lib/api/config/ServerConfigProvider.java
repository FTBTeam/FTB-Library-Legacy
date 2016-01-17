package ftb.lib.api.config;

import ftb.lib.mod.net.MessageEditConfigResponse;
import latmod.lib.config.*;

public class ServerConfigProvider implements IConfigProvider
{
	public final long adminToken;
	public final ConfigGroup group;
	
	public ServerConfigProvider(long t, ConfigGroup g)
	{
		adminToken = t;
		group = g;
	}
	
	public String getTitle()
	{ return group.getDisplayName(); }
	
	public String getGroupTitle(ConfigGroup g)
	{ return g.getDisplayName(); }
	
	public String getEntryTitle(ConfigEntry e)
	{ return e.ID; }
	
	public ConfigGroup getGroup()
	{ return group; }
	
	public void save()
	{ new MessageEditConfigResponse(this).sendToServer(); }
}