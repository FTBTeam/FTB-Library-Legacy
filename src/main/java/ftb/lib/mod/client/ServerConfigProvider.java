package ftb.lib.mod.client;

import ftb.lib.api.config.IConfigProvider;
import ftb.lib.mod.net.MessageEditConfigResponse;
import latmod.lib.IntList;
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
	{
		IntList list = new IntList();
		list.add(MessageEditConfigResponse.SEND_DATA);
		if(isTemp) list.add(MessageEditConfigResponse.TEMP);
		list.add(MessageEditConfigResponse.SAVE);
		new MessageEditConfigResponse(this, list.toArray()).sendToServer();
	}
	
	public void closed(boolean changed)
	{
		IntList list = new IntList();
		list.add(MessageEditConfigResponse.SEND_DATA);
		if(isTemp) list.add(MessageEditConfigResponse.TEMP);
		if(changed) list.add(MessageEditConfigResponse.SAVE);
		list.add(MessageEditConfigResponse.REMOVE);
		list.add(MessageEditConfigResponse.RELOAD);
		new MessageEditConfigResponse(this, list.toArray()).sendToServer();
	}
}