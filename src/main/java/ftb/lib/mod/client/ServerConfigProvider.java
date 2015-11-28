package ftb.lib.mod.client;

import cpw.mods.fml.relauncher.*;
import ftb.lib.api.config.IConfigProvider;
import ftb.lib.mod.net.MessageEditConfigResponse;
import latmod.lib.config.*;

@SideOnly(Side.CLIENT)
public class ServerConfigProvider implements IConfigProvider
{
	public final long adminToken;
	public final ConfigList list;
	
	public ServerConfigProvider(long t, ConfigList l)
	{ adminToken = t; list = l; }
	
	public String getTitle()
	{ return list.getDisplayName(); }
	
	public String getGroupTitle(ConfigGroup g)
	{ return g.getDisplayName(); }
	
	public String getEntryTitle(ConfigEntry e)
	{ return e.ID; }
	
	public ConfigList getList()
	{ return list; }
	
	public void save()
	{ new MessageEditConfigResponse(this).sendToServer(); }
}