package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.*;
import ftb.lib.api.*;
import ftb.lib.client.FTBLibClient;
import ftb.lib.mod.client.ServerConfigProvider;
import ftb.lib.mod.client.gui.GuiEditConfig;
import latmod.lib.config.ConfigList;

public class MessageEditConfig extends MessageLM
{
	public MessageEditConfig() { super(DATA_LONG); }
	
	public MessageEditConfig(long t, ConfigList list)
	{
		this();
		io.writeLong(t);
		io.writeString(list.ID);
		list.writeToIO(io, true);
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageContext ctx)
	{
		long token = io.readLong();
		String id = io.readString();
		ConfigList list = ConfigList.readFromIO(io, true);
		list.setID(id);
		FTBLibClient.mc.displayGuiScreen(new GuiEditConfig(null, new ServerConfigProvider(token, list)));
		return null;
	}
}