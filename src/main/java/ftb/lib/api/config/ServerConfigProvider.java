package ftb.lib.api.config;

import ftb.lib.ReloadType;
import ftb.lib.mod.net.MessageEditConfigResponse;

public final class ServerConfigProvider implements IConfigProvider
{
	private final long adminToken;
	private final ReloadType reload;
	private final ConfigGroup group;
	
	public ServerConfigProvider(long t, ReloadType r, ConfigGroup f)
	{
		adminToken = t;
		reload = r;
		group = f;
	}
	
	public String getTitle()
	{ return group.getDisplayName(); }
	
	@Override
	public String getGroupTitle(ConfigGroup g)
	{ return g.getID(); }
	
	@Override
	public String getEntryTitle(ConfigEntry e)
	{ return e.getID(); }
	
	@Override
	public ConfigGroup getConfigGroup()
	{ return group; }
	
	@Override
	public void save()
	{ new MessageEditConfigResponse(adminToken, reload, group).sendToServer(); }
}