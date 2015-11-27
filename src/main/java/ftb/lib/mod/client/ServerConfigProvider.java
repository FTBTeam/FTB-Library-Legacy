package ftb.lib.mod.client;

import cpw.mods.fml.relauncher.*;
import ftb.lib.api.config.IConfigProvider;
import ftb.lib.mod.net.MessageEditConfigResponse;
import latmod.lib.config.ConfigList;
import net.minecraft.util.*;

@SideOnly(Side.CLIENT)
public class ServerConfigProvider implements IConfigProvider
{
	public final long adminToken;
	public final ConfigList list;
	
	public ServerConfigProvider(long t, ConfigList l)
	{ adminToken = t; list = l; }
	
	public IChatComponent getTitle()
	{ return new ChatComponentText(list.ID); }
	
	public ConfigList getList()
	{ return list; }
	
	public void save()
	{ new MessageEditConfigResponse(this).sendToServer(); }
}