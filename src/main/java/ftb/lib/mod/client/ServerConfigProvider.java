package ftb.lib.mod.client;

import ftb.lib.api.config.IConfigProvider;
import ftb.lib.mod.net.MessageEditConfigResponse;
import latmod.lib.config.*;

public class ServerConfigProvider implements IConfigProvider
{
	public final long adminToken;
	public final boolean isTemp;
	public final ConfigGroup group;

	public ServerConfigProvider(long t, boolean temp, ConfigGroup g)
	{
		adminToken = t;
		isTemp = temp;
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
	{ new MessageEditConfigResponse(this, false, true).sendToServer(); }

	public void closed(boolean changed)
	{ new MessageEditConfigResponse(this, true, changed).sendToServer(); }
}